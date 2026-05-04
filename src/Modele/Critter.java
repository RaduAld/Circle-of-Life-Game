package Modele;

import java.util.Arrays;

public class Critter {

    // Nom du Critter affiché à l'utilisateur
    public final String name;

    // Rang dans la chaîne alimentaire - un Critter mange celui dont le rang = (rank - 1 + 12) % 12
    // Ordre : singleton(0) -> pair(1) -> 3-bend(2) -> 3-line(3) -> triangle(4) ->
    //            key(5) -> sled(6) -> arch(7) -> snake(8) -> diamond(9) ->
    //            propeller(10) -> 4-line(11) -> singleton(0) (boucle)
    public final int rank;

    // Nombre de cellules composant ce Critter
    public final int size;

    // Forme canonique : liste de coordonnées cube (x,y,z) relatives, triées lexicographiquement
    // Représentée à plat : [x0,y0,z0, x1,y1,z1, ...] pour un accès rapide
    // z est toujours explicite et respecte la contrainte x+y+z=0
    // Stockée dans sa forme minimale après normalisation sur les 6 rotations (polyhexes unilatéraux)
    // Note : on utilise 6 rotations sans réflexions car certaines formes comme sled/snake
    //        sont des images miroir l'une de l'autre et doivent rester distinctes
    public final int[] canonicalShape;

    // ------------------------------------------
    // Constructeur
    // ------------------------------------------

    // Construit un Critter à partir de sa forme brute en coordonnées cube (x, y, z)
    // Chaque cellule doit satisfaire x + y + z = 0
    public Critter(String name, int rank, int[][] rawCells) {
        this.name           = name;
        this.rank           = rank;
        this.size           = rawCells.length;
        this.canonicalShape = canonicalise(rawCells);
    }

    // ------------------------------------------
    // Normalisation canonique
    // ------------------------------------------

    // Les 6 rotations d'un hexagone en coordonnées axiales (x, y) :
    // On n'inclut PAS les réflexions car sled et snake sont des images miroir et doivent
    // être reconnus comme deux Critters distincts
    // La rotation s'applique sur (x,y), z est recalculé explicitement via z = -x-y
    private static final int[][][] ROTATIONS = {
            {{ 1,  0}, { 0,  1}},   // identité (0°)
            {{ 0, -1}, { 1,  1}},   // rotation 60°
            {{-1, -1}, { 1,  0}},   // rotation 120°
            {{-1,  0}, { 0, -1}},   // rotation 180°
            {{ 0,  1}, {-1, -1}},   // rotation 240°
            {{ 1,  1}, {-1,  0}},   // rotation 300°
    };

    // Retourne la forme canonique d'un ensemble de cellules en coordonnées cube (x, y, z) :
    // - applique les 6 rotations sur (x,y), recalcule z = -x-y explicitement
    // - traduit chaque variante de façon à ce que la première cellule (ordre lexicographique) soit en (0,0,0)
    // - retourne la variante lexicographiquement minimale sur (x,y,z)
    static int[] canonicalise(int[][] cells) {
        int[] best = null;

        for (int[][] rot : ROTATIONS) {
            int[][] transformed = new int[cells.length][3];
            for (int i = 0; i < cells.length; i++) {
                int x = cells[i][0];
                int y = cells[i][1];
                // applique la rotation 2D sur x et y
                int nx = rot[0][0] * x + rot[0][1] * y;
                int ny = rot[1][0] * x + rot[1][1] * y;
                // recalcule z explicitement - toujours valide car la rotation préserve x+y+z=0
                int nz = -nx - ny;
                transformed[i][0] = nx;
                transformed[i][1] = ny;
                transformed[i][2] = nz;
            }

            // trie par (x, y, z) pour une comparaison déterministe
            Arrays.sort(transformed, (a, b) ->
                    a[0] != b[0] ? a[0] - b[0] :
                            a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]
            );

            // translate pour que la première cellule (lexicographiquement minimale) soit à l'origine
            // on soustrait les coordonnées de cette première cellule à toutes les autres
            int ox = transformed[0][0];
            int oy = transformed[0][1];
            for (int[] c : transformed) {
                c[0] -= ox;
                c[1] -= oy;
                c[2] = -c[0] - c[1];   // recalcule z après translation
            }

            // re-trie après translation (l'ordre peut changer)
            Arrays.sort(transformed, (a, b) ->
                    a[0] != b[0] ? a[0] - b[0] :
                            a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]
            );

            // aplatit en tableau 1D [x0,y0,z0, x1,y1,z1, ...]
            int[] flat = flatten(transformed);

            // garde la variante lexicographiquement minimale
            if (best == null || compareLex(flat, best) < 0) {
                best = flat;
            }
        }

        return best;
    }

    // Aplatit un tableau de cellules cube en tableau 1D [x0,y0,z0, x1,y1,z1, ...]
    private static int[] flatten(int[][] cells) {
        int[] flat = new int[cells.length * 3];
        for (int i = 0; i < cells.length; i++) {
            flat[3 * i]     = cells[i][0];
            flat[3 * i + 1] = cells[i][1];
            flat[3 * i + 2] = cells[i][2];
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