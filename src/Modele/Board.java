package Modele;

import Patterns.Observable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board extends Observable {

    // ------------------------------------------
    // État du plateau - les deux masques sont tout ce qu'on stocke par instance
    // ------------------------------------------

    // Masque de bits du joueur 0 : le bit i est à 1 si le joueur 0 occupe la cellule d'indice spiral i
    public BigInteger p0;

    // Masque de bits du joueur 1 : même logique que p0
    public BigInteger p1;

    // Nombre de jetons capturés par chaque joueur au cours de la partie
    public int capturedByP0;
    public int capturedByP1;

    // ------------------------------------------
    // Tables statiques - partagées par toutes les instances, calculées une seule fois
    // ------------------------------------------

    // rayon du plateau : 4 anneaux autour du centre -> 61 cellules au total
    public static final int RADIUS = 4;

    // nombre total de cellules : 3r*r + 3r + 1 avec r=4
    public static final int CELL_COUNT = 61;

    // spiralCells[i] -> la cellule dont l'indice spiral est i
    public static Cell[] spiralCells;

    // cellToSpiral : Cell -> indice spiral (accès inverse de spiralCells)
    public static Map<Cell, Integer> cellToSpiral;

    // neighbourMasks[i] -> masque de bits des voisins de la cellule i utile pour consulter les voisins
    public static BigInteger[] neighbourMasks;

    // masque avec les 61 bits à 1 - représente le plateau entier
    public static BigInteger allCellsMask;

    // indique si les tables statiques ont déjà été construites
    static boolean tablesBuilt = false;

    // ------------------------------------------
    // Constructeurs
    // ------------------------------------------

    // Construit un plateau vide et initialise les tables statiques si nécessaire
    public Board() {
        this.p0           = BigInteger.ZERO;
        this.p1           = BigInteger.ZERO;
        this.capturedByP0 = 0;
        this.capturedByP1 = 0;

        // construction des tables une seule fois pour toutes les instances
        if (!tablesBuilt) {
            spiralCells    = new Cell[CELL_COUNT];
            cellToSpiral   = new HashMap<>();
            neighbourMasks = new BigInteger[CELL_COUNT];
            allCellsMask   = BigInteger.ONE.shiftLeft(CELL_COUNT).subtract(BigInteger.ONE);

            buildSpiralIndex();
            buildNeighbourMasks();
            tablesBuilt = true;
        }
    }

    // constructeur interne utilisé par copy() et applyMove() - ne reconstruit pas les tables
    private Board(BigInteger p0, BigInteger p1, int capturedByP0, int capturedByP1) {
        this.p0           = p0;
        this.p1           = p1;
        this.capturedByP0 = capturedByP0;
        this.capturedByP1 = capturedByP1;
    }

    // ------------------------------------------
    // Initialisation des tables statiques
    // ------------------------------------------

    // Remplit spiralCells[] et cellToSpiral dans l'ordre spiral du centre vers l'extérieur
    void buildSpiralIndex() {
        Cell[] order = BoardUtils.generateSpiralOrder(RADIUS);
        for (int i = 0; i < order.length; i++) {
            spiralCells[i] = order[i];
            cellToSpiral.put(order[i], i);
        }
    }

    // Pour chaque cellule, calcule le masque de bits de ses voisins valides
    // Appelé une fois au démarrage - O(61 * 6) opérations
    void buildNeighbourMasks() {
        for (int i = 0; i < CELL_COUNT; i++) {
            Cell c          = spiralCells[i];
            BigInteger mask = BigInteger.ZERO;

            for (int[] dir : Cell.HEX_DIRS) {
                // calcule le voisin candidat dans cette direction
                Cell neighbour = new Cell(c.x + dir[0], c.y + dir[1], c.z + dir[2]);
                if (cellToSpiral.containsKey(neighbour)) {
                    int nIdx = cellToSpiral.get(neighbour);
                    mask = mask.or(BigInteger.ONE.shiftLeft(nIdx));
                }
                // voisin hors plateau -> ignoré, la frontière est gérée naturellement
            }

            neighbourMasks[i] = mask;
        }
    }

    // ------------------------------------------
    // Accesseurs géométriques - le plateau comme source
    // ------------------------------------------

    // Retourne l'indice spiral de la cellule c, ou -1 si elle n'est pas sur le plateau
    public int spiralIndexOf(Cell c) {
        Integer idx = cellToSpiral.get(c);
        return idx != null ? idx : -1;
    }

    // Retourne la liste des voisins valides de c (max 6), filtrés aux limites du plateau
    public List<Cell> neighboursOf(Cell c) {
        List<Cell> result = new ArrayList<>();
        for (int[] dir : Cell.HEX_DIRS) {
            Cell candidate = new Cell(c.x + dir[0], c.y + dir[1], c.z + dir[2]);
            // la présence dans cellToSpiral suffit à valider la cellule
            if (cellToSpiral.containsKey(candidate)) {
                result.add(candidate);
            }
        }
        return result;
    }

    // ------------------------------------------
    // Accesseurs de l'état d'une cellule
    // ------------------------------------------

    // Retourne true si aucun joueur n'occupe cette cellule
    public boolean isEmpty(Cell c) {
        Integer idx = cellToSpiral.get(c);
        if (idx == null) return false;
        BigInteger bit = BigInteger.ONE.shiftLeft(idx);
        return p0.and(bit).equals(BigInteger.ZERO) && p1.and(bit).equals(BigInteger.ZERO);
    }

    // Retourne 0 si joueur 0, 1 si joueur 1, -1 si la cellule est vide ou hors plateau
    public int getOwner(Cell c) {
        Integer idx = cellToSpiral.get(c);
        if (idx == null) return -1;
        BigInteger bit = BigInteger.ONE.shiftLeft(idx);
        if (!p0.and(bit).equals(BigInteger.ZERO)) return 0;
        if (!p1.and(bit).equals(BigInteger.ZERO)) return 1;
        return -1;
    }

    // Retourne le masque de bits de toutes les cellules vides : ALL & ~p0 & ~p1
    public BigInteger getEmptyMask() {
        return allCellsMask.and(p0.not()).and(p1.not());
    }

    // Version lisible de getEmptyMask() - retourne une liste de Cell vides
    public List<Cell> getEmptyCells() {
        List<Cell> result = new ArrayList<>();
        for (int idx : BoardUtils.maskToIndices(getEmptyMask())) {
            result.add(spiralCells[idx]);
        }
        return result;
    }

    // Retourne le nombre de jetons actuellement posés sur le plateau pour ce joueur
    public int getTokenCount(int player) {
        return BoardUtils.popCount(player == 0 ? p0 : p1);
    }

    // ------------------------------------------
    // Application des mouvements
    // ------------------------------------------

    // Retourne un nouveau Board avec le jeton du joueur posé en c
    // Immuable - ne modifie pas l'instance courante - résout les captures automatiquement
    public Board applyMove(Cell c, int player) {
        Integer idx = cellToSpiral.get(c);
        if (idx == null) return this;
        notifierObservateur();
        return applyMoveBit(idx, player);
    }

    // Identique à applyMove() mais prend directement un indice spiral
    // Évite l'allocation de Cell dans les boucles internes du solveur
    public Board applyMoveBit(int spiralIdx, int player) {
        BigInteger bit = BigInteger.ONE.shiftLeft(spiralIdx);

        // le coup n'est légal que sur une cellule vide
        if (!p0.and(bit).equals(BigInteger.ZERO) || !p1.and(bit).equals(BigInteger.ZERO)) {
            return this;
        }

        // pose le jeton : crée de nouveaux masques sans modifier les originaux
        BigInteger newP0 = player == 0 ? p0.or(bit) : p0;
        BigInteger newP1 = player == 1 ? p1.or(bit) : p1;
        Board next       = new Board(newP0, newP1, capturedByP0, capturedByP1);

        // résolution des captures : cherche les groupes ennemis adjacents mangés
        int enemy                    = 1 - player;
        BigInteger capturedAll       = BigInteger.ZERO;
        List<BigInteger> myGroups    = next.getAllGroups(player);
        List<BigInteger> enemyGroups = next.getAllGroups(enemy);

        for (BigInteger enemyGroup : enemyGroups) {
            // identifie le Critter ennemie à partir de ses cellules
            Critter enemyCritters = CritterRegistry.identify(next.groupToCells(enemyGroup));
            if (enemyCritters == null) continue;

            for (BigInteger myGroup : myGroups) {
                // identifie mon Critter
                Critter myCritter = CritterRegistry.identify(next.groupToCells(myGroup));
                if (myCritter == null) continue;

                // capture si mon Critter mange le Critter ennemie ET les groupes sont adjacents
                if (CritterRegistry.eats(myCritter, enemyCritters)
                        && next.areGroupsAdjacent(myGroup, enemyGroup)) {
                    capturedAll = capturedAll.or(enemyGroup);
                    break;
                }
            }
        }

        // Supprime les cellules capturées et met à jour le compteur
        if (!capturedAll.equals(BigInteger.ZERO)) {
            int capturedCount = BoardUtils.popCount(capturedAll);
            next = next.removeCaptured(capturedAll, enemy);
            if (player == 0) next.capturedByP0 += capturedCount;
            else             next.capturedByP1 += capturedCount;
        }

        return next;
    }

    // Efface tous les bits de capturedMask du masque du joueur donné - retourne un nouveau Board immuable
    public Board removeCaptured(BigInteger capturedMask, int player) {
        BigInteger newP0 = player == 0 ? p0.andNot(capturedMask) : p0;
        BigInteger newP1 = player == 1 ? p1.andNot(capturedMask) : p1;
        return new Board(newP0, newP1, capturedByP0, capturedByP1);
    }

    // ------------------------------------------
    // Détection des groupes
    // ------------------------------------------

    // Retourne le masque de toutes les cellules connectées à startIdx appartenant à player
    // Coeur de la détection de Critters - expansion par vagues via les masques précalculés
    public BigInteger floodFill(int startIdx, int player) {
        BigInteger playerMask = player == 0 ? p0 : p1;
        BigInteger group      = BigInteger.ONE.shiftLeft(startIdx);
        BigInteger frontier   = group;

        while (!frontier.equals(BigInteger.ZERO)) {
            BigInteger expanded = BigInteger.ZERO;

            // pour chaque cellule sur la frontière, ajoute ses voisins via le masque précalculé
            BigInteger temp = frontier;
            while (!temp.equals(BigInteger.ZERO)) {
                int idx  = temp.getLowestSetBit();
                expanded = expanded.or(neighbourMasks[idx]);
                temp     = temp.clearBit(idx);
            }

            // nouvelle frontière = voisins appartenant au joueur, pas encore visités
            frontier = expanded.and(playerMask).andNot(group);
            group    = group.or(frontier);
        }

        return group;
    }

    // Retourne toutes les composantes connexes du joueur sous forme de liste de masques de bits
    public List<BigInteger> getAllGroups(int player) {
        BigInteger playerMask   = player == 0 ? p0 : p1;
        BigInteger visited      = BigInteger.ZERO;
        List<BigInteger> groups = new ArrayList<>();

        BigInteger temp = playerMask;
        while (!temp.equals(BigInteger.ZERO)) {
            int idx = temp.getLowestSetBit();
            if (!visited.testBit(idx)) {
                BigInteger group = floodFill(idx, player);
                groups.add(group);
                visited = visited.or(group);
            }
            temp = temp.clearBit(idx);
        }

        return groups;
    }

    // Convertit un masque de bits de groupe en une liste de Cell - pour l'affichage et la vérification des règles
    public List<Cell> groupToCells(BigInteger groupMask) {
        List<Cell> cells = new ArrayList<>();
        for (int idx : BoardUtils.maskToIndices(groupMask)) {
            cells.add(spiralCells[idx]);
        }
        return cells;
    }

    // ------------------------------------------
    // Adjacence entre groupes
    // ------------------------------------------

    // Retourne true si au moins une cellule de g1 a un voisin dans g2 - O(61) opérations binaires
    public boolean areGroupsAdjacent(BigInteger g1, BigInteger g2) {
        BigInteger expanded = BigInteger.ZERO;
        BigInteger temp     = g1;
        while (!temp.equals(BigInteger.ZERO)) {
            int idx  = temp.getLowestSetBit();
            expanded = expanded.or(neighbourMasks[idx]);
            temp     = temp.clearBit(idx);
        }
        return !expanded.and(g2).equals(BigInteger.ZERO);
    }

    // Retourne le masque des cellules ennemies adjacentes au groupe donné - détecte les menaces de prédation
    public BigInteger getAdjacentEnemyMask(BigInteger group, int player) {
        BigInteger enemyMask = player == 0 ? p1 : p0;
        BigInteger expanded  = BigInteger.ZERO;
        BigInteger temp      = group;

        while (!temp.equals(BigInteger.ZERO)) {
            int idx  = temp.getLowestSetBit();
            expanded = expanded.or(neighbourMasks[idx]);
            temp     = temp.clearBit(idx);
        }
        return expanded.and(enemyMask);
    }

    // ------------------------------------------
    // Hachage et support pour solver
    // ------------------------------------------

    // Retourne un hash rapide de l'état du plateau pour les tables de transposition
    public long zobristHash() {
        // p0 dans les bits bas, p1 décalé - XOR pour combiner les deux masques en un long
        return p0.longValue() ^ p1.shiftLeft(1).longValue();
    }

    // Retourne une copie de cet état - BigInteger étant immuable, aucune copie profonde n'est nécessaire
    public Board copy() {
        return new Board(p0, p1, capturedByP0, capturedByP1);
    }

    // Retourne true si p0 et p1 sont identiques entre les deux plateaux - même état de jeu
    /*
    public boolean equals(Board other) {
        return this.p0.equals(other.p0) && this.p1.equals(other.p1);
    }
    */

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Board)) return false;
        Board o = (Board) other;
        return this.p0.equals(o.p0) && this.p1.equals(o.p1);
    }

    // ------------------------------------------
    // Condition de victoire
    // ------------------------------------------

    // Retourne 0 si joueur 0 gagne, 1 si joueur 1 gagne, -1 si la partie continue
    // La victoire se déclenche par captures >= captureThreshold ou absence de coups légaux
    public int checkWinner(int captureThreshold) {
        if (capturedByP0 >= captureThreshold) return 0;
        if (capturedByP1 >= captureThreshold) return 1;
        if (!hasLegalMoves()) {
            if (capturedByP0 > capturedByP1) return 0;
            if (capturedByP1 > capturedByP0) return 1;
        }
        return -1;
    }

    // Retourne true s'il reste au moins une cellule vide pour poser un jeton
    public boolean hasLegalMoves() {
        return !getEmptyMask().equals(BigInteger.ZERO);
    }
}