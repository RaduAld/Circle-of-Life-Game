package Modele;

import java.util.Objects;

public class Cell {

    // Coordonnées cube : trois axes à 120 deg les uns des autres
    // La contrainte x + y + z = 0 est assurée par construction
    public int x, y, z;

    // Les 6 directions de déplacement en coordonnées cube
    // Chaque direction modifie deux axes de +-1 et laisse le troisième inchangé
    public static final int[][] HEX_DIRS = {
            { 1,  0, -1},   // - droite
            { 1, -1,  0},   // - haut-droite
            { 0, -1,  1},   // - haut-gauche
            {-1,  0,  1},   // - gauche
            {-1,  1,  0},   // - bas-gauche
            { 0,  1, -1}    // - bas-droite
    };

    // ------------------------------------------
    // Constructeur
    // ------------------------------------------

    // Construit une cellule avec ses trois coordonnées cube explicites
    public Cell(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ------------------------------------------
    // Géométrie pure - aucune dépendance extérieure
    // ------------------------------------------

    // Retourne l'anneau de cette cellule : 0 pour le centre, 4 pour le bord externe
    // Calculé uniquement depuis les coordonnées - pas besoin du plateau
    public int ring() {
        return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
    }

    // Distance hex entre cette cellule et other - formule des coordonnées cube
    public int distanceTo(Cell other) {
        return Math.max(
                Math.abs(this.x - other.x),
                Math.max(
                        Math.abs(this.y - other.y),
                        Math.abs(this.z - other.z)
                )
        );
    }

    // Retourne true si cette cellule et other partagent une arête (distance == 1)
    public boolean isAdjacent(Cell other) {
        return distanceTo(other) == 1;
    }

    // Retourne la notation humaine de cette cellule
    // Lettre = ligne de haut en bas : y=-4 -> A, y=0 -> E, y=4 -> I
    // Chiffre = position dans la ligne de gauche à droite, démarrant à 0
    // Exemple : centre (x=0, y=0, z=0) -> E4
    public String toNotation() {
        char row = (char) ('A' + (y + 4));
        int  col = x - Math.max(-4, -y - 4);
        return "" + row + col;
    }

    // ------------------------------------------
    // Égalité et hachage - nécessaires pour l'utilisation en clé de Map
    // ------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell c = (Cell) o;
        return this.x == c.x && this.y == c.y && this.z == c.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Cell(x=" + x + ", y=" + y + ", z=" + z + ")";
    }

    // ------------------------------------------
    // Fonction for AI
    // ------------------------------------------

    public static Cell getCellule(String avant, String apres, int AIplayer){
        String[] listeAvant = avant.split("_");
        String[] listeApres = apres.split("_");
        String sAvant = listeAvant[AIplayer];
        String sApres = listeApres[AIplayer];
        for(int i=0; i<Board.CELL_COUNT; i++){
            if (sAvant.charAt(i) != sApres.charAt(i)){
                return Board.spiralCells[i];
            }
        }
        return null;
    }
}