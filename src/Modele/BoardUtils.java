package Modele;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BoardUtils {

    // ------------------------------------------
    // Génération de l'ordre spiral
    // ------------------------------------------

    // Produit le tableau de 61 cellules dans l'ordre spiral du centre vers l'extérieur
    // Anneau 0 : centre, anneau k : 6k cellules - total 3r*r+3r+1 avec r=4
    public static Cell[] generateSpiralOrder(int radius) {
        List<Cell> order = new ArrayList<>();

        // anneau 0 : uniquement le centre
        order.add(new Cell(0, 0, 0));

        for (int ring = 1; ring <= radius; ring++) {
            // Point de départ de l'anneau ring
            int x = -ring;
            int y =  ring;
            int z =  0;

            // parcourt les 6 côtés en suivant HEX_DIRS dans l'ordre
            // chaque côté contient exactement ring cellules
            for (int side = 0; side < 6; side++) {
                int[] d = Cell.HEX_DIRS[side];
                for (int step = 0; step < ring; step++) {
                    order.add(new Cell(x, y, z));
                    x += d[0];
                    y += d[1];
                    z += d[2];
                }
            }
        }

        // conversion du ArrayList en Cell[n]
        return order.toArray(new Cell[0]);
    }

    // ------------------------------------------
    // Utilitaires binaires
    // ------------------------------------------

    // Compte le nombre de bits à 1 dans le masque - délègue à BigInteger.bitCount()
    public static int popCount(BigInteger mask) {
        return mask.bitCount();
    }

    // Retourne les indices spiraux de tous les bits à 1 du masque sous forme d'un tableau d'entiers
    public static int[] maskToIndices(BigInteger mask) {
        int count     = mask.bitCount();
        int[] indices = new int[count];
        int pos       = 0;
        BigInteger temp = mask;
        while (!temp.equals(BigInteger.ZERO)) {
            // getLowestSetBit() retourne l'indice du bit de poids faible à 1
            int idx        = temp.getLowestSetBit();
            indices[pos++] = idx;
            temp           = temp.clearBit(idx);
        }
        return indices;
    }

    // ------------------------------------------
    // Affichage ASCII
    // ------------------------------------------

    // Produit un rendu ASCII du plateau - O = joueur 0, X = joueur 1, · = vide
    public static String boardToString(Board b) {
        StringBuilder sb = new StringBuilder();
        int radius       = Board.RADIUS;

        // parcourt les lignes de y=-radius à y=+radius (axe y = axe vertical)
        for (int y = -radius; y <= radius; y++) {
            // Indentation proportionnelle pour simuler la forme hexagonale
            sb.append(" ".repeat(Math.abs(y)));

            // bornes de x pour cette ligne : seules les cellules avec |z|<=radius sont valides
            int xMin = Math.max(-radius, -y - radius);
            int xMax = Math.min( radius, -y + radius);

            for (int x = xMin; x <= xMax; x++) {
                // z est le troisième axe : z = -x - y par construction du plateau
                int z       = -x - y;
                Cell c      = new Cell(x, y, z);
                Integer idx = b.cellToSpiral.get(c);
                if (idx == null) {
                    sb.append("? ");
                } else {
                    BigInteger bit = BigInteger.ONE.shiftLeft(idx);
                    if      (!b.p0.and(bit).equals(BigInteger.ZERO)) sb.append("O ");
                    else if (!b.p1.and(bit).equals(BigInteger.ZERO)) sb.append("X ");
                    else                                               sb.append("· ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // ------------------------------------------
    // Notation humaine
    // ------------------------------------------

    // Convertit une notation humaine (ex: "E5") en Cell
    // Colonne lettre : A = x=-4, E = x=0 - Ligne chiffre : 1 = y=-4, 5 = y=0
    public static Cell fromNotation(String notation) {
        if (notation == null || notation.length() < 2) return null;
        int x = Character.toUpperCase(notation.charAt(0)) - 'A' - 4;
        int y = Integer.parseInt(notation.substring(1)) - 5;
        int z = -x - y;
        return new Cell(x, y, z);
    }
}