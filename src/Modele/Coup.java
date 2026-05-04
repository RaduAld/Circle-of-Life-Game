package Modele;

public class Coup {
    public Cell cellule;
    public int joueur;
    public Board boardAvant;  // snapshot AVANT le coup, pour le undo

    public Coup(Cell cellule, int joueur, Board boardAvant) {
        this.cellule    = cellule;
        this.joueur     = joueur;
        this.boardAvant = boardAvant.copy();
    }

    int changePlayer(int player){
        return ( player+1 ) % 2;
    }
}
