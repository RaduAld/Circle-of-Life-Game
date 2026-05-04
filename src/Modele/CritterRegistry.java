package Modele;

import java.util.List;

public class CritterRegistry {

    // ------------------------------------------
    // Les 12 Critters — définis par leur rang, nom, et forme canonique
    //
    // La chaîne alimentaire est une boucle de 12 :
    // chaque Critter mange celui dont le rang est (son rang - 1) modulo 12
    //
    //  Rang  Nom           Taille  Structure
    //   0    singleton       1     une cellule seule
    //   1    pair            2     deux cellules adjacentes
    //   2    3-bend          3     coude : A touche B et C, B et C non adjacents
    //   3    3-line          3     ligne droite
    //   4    triangle        3     toutes les 3 mutuellement adjacentes
    //   5    key             4     L-shape : ligne de 3 + 1 à UNE EXTRÉMITÉ
    //   6    sled            4     S-shape chiral 1 (zigzag décalé)
    //   7    arch            4     T-shape : ligne de 3 + 1 AU MILIEU (+ arête extra)
    //   8    snake           4     S-shape chiral 2 (miroir du sled)
    //   9    diamond         4     losange compact (5 arêtes, degré max 3)
    //  10    propeller       4     Y-shape : 1 centre touche les 3 autres, eux non adjacents
    //  11    4-line          4     ligne droite de 4
    // ------------------------------------------

    public static final Critter SINGLETON  = new Critter("singleton",  0,  new int[][]{{0,0,0}});
    public static final Critter PAIR       = new Critter("pair",       1,  new int[][]{{0,0,0},{0,1,-1}});
    public static final Critter BEND3      = new Critter("3-bend",     2,  new int[][]{{0,0,0},{0,1,-1},{1,-1,0}});
    public static final Critter LINE3      = new Critter("3-line",     3,  new int[][]{{0,0,0},{0,1,-1},{0,2,-2}});
    public static final Critter TRIANGLE   = new Critter("triangle",   4,  new int[][]{{0,0,0},{0,1,-1},{1,0,-1}});
    public static final Critter KEY        = new Critter("key",        5,  new int[][]{{0,0,0},{0,1,-1},{0,2,-2},{1,-1,0}});
    public static final Critter SLED       = new Critter("sled",       6,  new int[][]{{0,0,0},{0,1,-1},{1,-1,0},{1,1,-2}});
    public static final Critter ARCH       = new Critter("arch",       7,  new int[][]{{0,0,0},{0,1,-1},{0,2,-2},{1,1,-2}});
    public static final Critter SNAKE      = new Critter("snake",      8,  new int[][]{{0,0,0},{0,1,-1},{1,-2,1},{1,-1,0}});
    public static final Critter DIAMOND    = new Critter("diamond",    9,  new int[][]{{0,0,0},{0,1,-1},{1,-1,0},{1,0,-1}});
    public static final Critter PROPELLER  = new Critter("propeller",  10, new int[][]{{0,0,0},{1,-2,1},{1,-1,0},{2,-1,-1}});
    public static final Critter LINE4      = new Critter("4-line",     11, new int[][]{{0,0,0},{0,1,-1},{0,2,-2},{0,3,-3}});

    // Tableau ordonné par rang — ALL[i].rank == i
    public static final Critter[] ALL = {
            SINGLETON, PAIR, BEND3, LINE3, TRIANGLE,
            KEY, SLED, ARCH, SNAKE, DIAMOND, PROPELLER, LINE4
    };

    // Chaîne alimentaire explicite — FOOD_CHAIN[i] est mangé par ALL[i]
    // Lire comme : ALL[i] mange FOOD_CHAIN[i]
    //
    //   ALL[0]  singleton  mange  LINE4      (rank 11)
    //   ALL[1]  pair       mange  SINGLETON  (rank  0)
    //   ALL[2]  3-bend     mange  PAIR       (rank  1)
    //   ALL[3]  3-line     mange  BEND3      (rank  2)
    //   ALL[4]  triangle   mange  LINE3      (rank  3)
    //   ALL[5]  key        mange  TRIANGLE   (rank  4)
    //   ALL[6]  sled       mange  KEY        (rank  5)
    //   ALL[7]  arch       mange  SLED       (rank  6)
    //   ALL[8]  snake      mange  ARCH       (rank  7)
    //   ALL[9]  diamond    mange  SNAKE      (rank  8)
    //   ALL[10] propeller  mange  DIAMOND    (rank  9)
    //   ALL[11] 4-line     mange  PROPELLER  (rank 10)

    // Vérification d'intégrité au chargement de la classe
    static {
        // chaque forme canonique doit être unique
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Critter c : ALL) {
            String key = java.util.Arrays.toString(c.canonicalShape);
            if (!seen.add(key))
                throw new IllegalStateException("Collision canonique : " + c.name);
        }
        // le tableau doit être ordonné par rang
        for (int i = 0; i < ALL.length; i++)
            if (ALL[i].rank != i)
                throw new IllegalStateException("Rang incorrect à l'index " + i + " : " + ALL[i].name);
    }

    // ------------------------------------------
    // Identification
    // ------------------------------------------

    // Retourne le Critter correspondant à ce groupe de cellules, ou null si aucun ne correspond
    public static Critter identify(List<Cell> cells) {
        if (cells == null || cells.isEmpty() || cells.size() > 4) return null;
        int[] groupCanon = Critter.canonicalise(toRelative(cells));
        for (Critter c : ALL)
            if (java.util.Arrays.equals(c.canonicalShape, groupCanon)) return c;
        return null;
    }

    // Convertit une liste de Cell en coordonnées cube relatives à la première cellule
    private static int[][] toRelative(List<Cell> cells) {
        int ox = cells.get(0).x, oy = cells.get(0).y, oz = cells.get(0).z;
        int[][] raw = new int[cells.size()][3];
        for (int i = 0; i < cells.size(); i++) {
            raw[i][0] = cells.get(i).x - ox;
            raw[i][1] = cells.get(i).y - oy;
            raw[i][2] = cells.get(i).z - oz;
        }
        return raw;
    }

    // ------------------------------------------
    // Relation prédateur / proie
    // ------------------------------------------

    // Retourne true si predator mange prey
    // predator de rang R mange prey de rang (R - 1 + 12) % 12
    public static boolean eats(Critter predator, Critter prey) {
        if (predator == null || prey == null) return false;
        return prey.rank == Math.floorMod(predator.rank - 1, ALL.length);
    }

    // Retourne la proie directe de predator
    public static Critter preyOf(Critter predator) {
        if (predator == null) return null;
        return ALL[Math.floorMod(predator.rank - 1, ALL.length)];
    }

    // Retourne le prédateur direct de prey
    public static Critter predatorOf(Critter prey) {
        if (prey == null) return null;
        return ALL[(prey.rank + 1) % ALL.length];
    }
}