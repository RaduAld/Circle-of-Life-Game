package Modele;

import java.util.Arrays;

public class Critter {

    // Nom du Critter affiché à l'utilisateur
    public final String name;

    // Rang dans la chaîne alimentaire - un Critter mange celle dont le rang = (rank - 1 + 13) % 13
    // Ordre : singleton(0) -> pair(1) -> 3-bend(2) -> 3-line(3) -> triangle(4) ->
    //            key(5) -> sled(6) -> arch(7) -> snake(8) -> diamond(9) ->
    //            propeller(10) -> 4-line(11) -> singleton(0) (boucle)
    public final int rank;

    // Nombre de cellules composant ce Critter
    public final int size;

    // Forme canonique : liste de coordonnées (x,y) relatives, triées en ordre lexicographiquement
    // Représentée à plat : [x0, y0, x1, y1, ...] pour un accès rapide
    // Stockée dans sa forme minimale après normalisation toutes rotations/réflexions confondues
    public final int[] canonicalShape;

    // ------------------------------------------
    // Constructeur
    // ------------------------------------------

    // Construit une espèce à partir de sa forme brute
    public Critter(String name, int rank, int[][] rawCells) {
        this.name          = name;
        this.rank          = rank;
        this.size          = rawCells.length;
        this.canonicalShape = canonicalise(rawCells);
    }

    // ------------------------------------------
    // Normalisation canonique
    // ------------------------------------------

    // Les 12 symétries d'un hexagone plat : 6 rotations × 2 (avec réflexion)
    // Chaque symétrie est une matrice 2x2 appliquée aux coordonnées (x, y)
    // En coordonnées cube (x+y+z=0), les rotations de 60° deviennent des permutations/négations
    private static final int[][][] SYMMETRIES = {
            // 6 rotations (0°, 60°, 120°, 180°, 240°, 300°)
            {{ 1,  0}, { 0,  1}},   // - identité
            {{ 0, -1}, { 1,  1}},   // - rotation 60°
            {{-1, -1}, { 1,  0}},   // - rotation 120°
            {{-1,  0}, { 0, -1}},   // - rotation 180°
            {{ 0,  1}, {-1, -1}},   // - rotation 240°
            {{ 1,  1}, {-1,  0}},   // - rotation 300°

            // 6 réflexions (identité + réflexion, puis rotations de la réflexion)
            {{ 1,  0}, { 0, -1}},   // - réflexion axe x
            {{ 0,  1}, { 1,  0}},   // - réflexion axe y=x
            {{-1,  0}, { 1,  1}},   // - réflexion composée
            {{-1, -1}, { 0,  1}},   // - réflexion composée
            {{ 0, -1}, {-1,  0}},   // - réflexion composée
            {{ 1,  1}, { 0, -1}},   // - réflexion composée
    };

    // Retourne la forme canonique d'un ensemble de cellules :
    // applique les 12 symétries, traduit chaque variante à l'origine, retourne la plus petite lexicographiquement
    static int[] canonicalise(int[][] cells) {
        int[] best = null;

        for (int[][] sym : SYMMETRIES) {
            // applique la symétrie à toutes les cellules
            int[][] transformed = new int[cells.length][2];
            for (int i = 0; i < cells.length; i++) {
                int x = cells[i][0];
                int y = cells[i][1];
                transformed[i][0] = sym[0][0] * x + sym[0][1] * y;
                transformed[i][1] = sym[1][0] * x + sym[1][1] * y;
            }

            // trie les cellules pour une comparaison déterministe
            Arrays.sort(transformed, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

            // translate au minimum : soustrait la plus petite coordonnée de chaque axe
            int minX = transformed[0][0];
            int minY = Integer.MAX_VALUE;
            for (int[] c : transformed) minY = Math.min(minY, c[1]);
            for (int[] c : transformed) { c[0] -= minX; c[1] -= minY; }

            // re-trie après translation
            Arrays.sort(transformed, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

            // aplatit en tableau 1D pour comparaison et stockage
            int[] flat = flatten(transformed);

            // garde la variante lexicographiquement minimale
            if (best == null || compareLex(flat, best) < 0) {
                best = flat;
            }
        }

        return best;
    }

    // Aplatit un tableau 2D de coordonnées en tableau 1D [x0,y0,x1,y1,...]
    private static int[] flatten(int[][] cells) {
        int[] flat = new int[cells.length * 2];
        for (int i = 0; i < cells.length; i++) {
            flat[2 * i]     = cells[i][0];
            flat[2 * i + 1] = cells[i][1];
        }
        return flat;
    }

    // Comparaison lexicographique de deux tableaux d'entiers
    private static int compareLex(int[] a, int[] b) {
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            if (a[i] != b[i]) return a[i] - b[i];
        }
        return a.length - b.length;
    }

    @Override
    public String toString() {
        return name + "(rank=" + rank + ", size=" + size + ")";
    }
}