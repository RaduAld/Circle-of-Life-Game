package Modele;

import java.util.List;
import java.util.ArrayList;

public class CritterRegistry {

    // ------------------------------------------
    // Chaîne alimentaire complète - 13 espèces en boucle
    // ------------------------------------------
    //
    //  Rang  Nom          Taille  Mange
    //   0    singleton      1     4-line (rang 11)
    //   1    pair           2     singleton (rang 0)
    //   2    3-bend         3     pair (rang 1)
    //   3    3-line         3     3-bend (rang 2)
    //   4    triangle       3     3-line (rang 3)
    //   5    key            4     triangle (rang 4)
    //   6    sled           4     key (rang 5)
    //   7    arch           4     sled (rang 6)
    //   8    snake          4     arch (rang 7)
    //   9    diamond        4     snake (rang 8)
    //  10    propeller      4     diamond (rang 9)
    //  11    4-line         4     propeller (rang 10)
    //  12    (boucle vers singleton)
    //
    // Source : Board Game Arena - Circle of Life rules
    // Une espèce de rang R mange l'espèce de rang (R - 1 + 13) % 13

    // ------------------------------------------
    // Définition des formes en coordonnées cube (x, y) - z = -x-y implicite
    // Chaque tableau int[][] est une liste de cellules relatives à la forme
    // La normalisation canonique est appliquée automatiquement par Species()
    // ------------------------------------------

    // Monohex (1 cellule)
    // singleton : une seule cellule isolée
    private static final int[][] SHAPE_SINGLETON = {
            {0, 0}
    };

    // Dihex (2 cellules)
    // pair : deux cellules adjacentes
    private static final int[][] SHAPE_PAIR = {
            {0, 0}, {1, 0}
    };

    // Trihex (3 cellules) - il existe 3 formes libres distinctes
    // 3-bend : angle à 120° (forme en L)
    //       O
    //      O O
    private static final int[][] SHAPE_3BEND = {
            {0, 0}, {1, 0}, {0, 1}
    };

    // 3-line : trois cellules en ligne droite
    //      O O O
    private static final int[][] SHAPE_3LINE = {
            {0, 0}, {1, 0}, {2, 0}
    };

    // triangle : trois cellules formant un triangle compact (triplet compact)
    //       O
    //      O O    (triangle hexagonal - voisins deux-à-deux)
    private static final int[][] SHAPE_TRIANGLE = {
            {0, 0}, {1, 0}, {0, -1}
    };

    // Tétrahex (4 cellules) - il existe 7 formes libres distinctes
    // key : forme en L (3 en ligne + 1 perpendiculaire au bout)
    //      O
    //      O
    //      O O
    private static final int[][] SHAPE_KEY = {
            {0, 0}, {1, 0}, {2, 0}, {2, 1}
    };

    // sled : forme en S/Z (deux paires décalées)
    //       O O
    //      O O
    private static final int[][] SHAPE_SLED = {
            {0, 0}, {1, 0}, {1, 1}, {2, 1}
    };

    // arch : forme en U (3 en ligne + 1 perpendiculaire au milieu)
    //      O O O
    //        O
    private static final int[][] SHAPE_ARCH = {
            {0, 0}, {1, 0}, {2, 0}, {1, -1}
    };

    // snake : forme en Z allongée (4 en ligne brisée)
    //        O O
    //      O O
    private static final int[][] SHAPE_SNAKE = {
            {0, 0}, {1, 0}, {0, -1}, {1, 1}
    };

    // diamond : quatre cellules formant un losange compact
    //       O O
    //      O O
    //   (carré hexagonal 2x2)
    private static final int[][] SHAPE_DIAMOND = {
            {0, 0}, {1, 0}, {0, 1}, {1, -1}
    };

    // propeller : triangle + 1 cellule en extension (forme asymétrique à 3 branches)
    //        O
    //       O O
    //      O
    private static final int[][] SHAPE_PROPELLER = {
            {0, 0}, {1, 0}, {0, 1}, {-1, 1}
    };

    // 4-line : quatre cellules en ligne droite
    //      O O O O
    private static final int[][] SHAPE_4LINE = {
            {0, 0}, {1, 0}, {2, 0}, {3, 0}
    };

    // ------------------------------------------
    // Catalogue complet - construit une seule fois au chargement de la classe
    // ------------------------------------------

    // Liste ordonnée par rang - ALL.get(i).rank == i
    public static final List<Critter> ALL = buildRegistry();

    private static List<Critter> buildRegistry() {
        List<Critter> list = new ArrayList<>();
        // L'ordre de création doit correspondre au rang
        list.add(new Critter("singleton",  0,  SHAPE_SINGLETON));
        list.add(new Critter("pair",       1,  SHAPE_PAIR));
        list.add(new Critter("3-bend",     2,  SHAPE_3BEND));
        list.add(new Critter("3-line",     3,  SHAPE_3LINE));
        list.add(new Critter("triangle",   4,  SHAPE_TRIANGLE));
        list.add(new Critter("key",        5,  SHAPE_KEY));
        list.add(new Critter("sled",       6,  SHAPE_SLED));
        list.add(new Critter("arch",       7,  SHAPE_ARCH));
        list.add(new Critter("snake",      8,  SHAPE_SNAKE));
        list.add(new Critter("diamond",    9,  SHAPE_DIAMOND));
        list.add(new Critter("propeller",  10, SHAPE_PROPELLER));
        list.add(new Critter("4-line",     11, SHAPE_4LINE));
        return list;
    }

    // ------------------------------------------
    // Identification d'une espèce à partir d'un groupe de cellules
    // ------------------------------------------

    // Retourne l'espèce correspondant à ce groupe de cellules, ou null si aucune espèce ne correspond
    // Un groupe de plus de 4 cellules ne peut jamais être une espèce valide (règle du jeu)
    public static Critter identify(List<Cell> cells) {
        if (cells == null || cells.size() == 0 || cells.size() > 4) return null;

        // Convertit les Cell en coordonnées relatives (x, y) centrées sur la première cellule
        int[][] raw = toRelative(cells);

        // Calcule la forme canonique de ce groupe
        int[] groupCanon = Critter.canonicalise(raw);

        // Cherche dans le catalogue l'espèce dont la forme canonique correspond
        for (Critter s : ALL) {
            if (java.util.Arrays.equals(s.canonicalShape, groupCanon)) {
                return s;
            }
        }

        // Aucune correspondance - groupe de forme inconnue (ne devrait pas arriver avec 1-4 cellules)
        return null;
    }

    // Convertit une liste de Cell en tableau de coordonnées (x, y) relatives
    // Les coordonnées sont exprimées par rapport à la première cellule du groupe
    private static int[][] toRelative(List<Cell> cells) {
        int ox = cells.get(0).x;
        int oy = cells.get(0).y;
        int[][] raw = new int[cells.size()][2];
        for (int i = 0; i < cells.size(); i++) {
            raw[i][0] = cells.get(i).x - ox;
            raw[i][1] = cells.get(i).y - oy;
        }
        return raw;
    }

    // ------------------------------------------
    // Relation prédateur / proie
    // ------------------------------------------

    // Retourne true si predator mange prey selon la chaîne alimentaire
    // predator mange prey si prey.rank == (predator.rank - 1 + 13) % 13
    public static boolean eats(Critter predator, Critter prey) {
        if (predator == null || prey == null) return false;
        return prey.rank == Math.floorMod(predator.rank - 1, ALL.size());
    }

    // Retourne l'espèce que predator peut manger (sa proie directe)
    public static Critter preyOf(Critter predator) {
        if (predator == null) return null;
        int preyRank = Math.floorMod(predator.rank - 1, ALL.size());
        return ALL.get(preyRank);
    }

    // Retourne l'espèce qui mange predator (son prédateur direct)
    public static Critter predatorOf(Critter prey) {
        if (prey == null) return null;
        int predRank = (prey.rank + 1) % ALL.size();
        return ALL.get(predRank);
    }
}