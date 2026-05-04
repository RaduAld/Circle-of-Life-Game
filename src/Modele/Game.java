package Modele;

public class Game {
    Board board;
    Historic historique;
    int currentPlayer;

    //On choisit le joueur qui démarre
    Game(int joueur){
        board = new Board();
        historique = new Historic();
        if (joueur > 1 || joueur<0){
            currentPlayer = 0;
            System.out.println("Erreur, le joueur ne peut être que 0 ou 1.");
        }
        else{
            currentPlayer = joueur;
        }
    }
    //Pour démarer une partie sauvegardée dans un fichier
    Game(String path){
        board = new Board();
        historique = new Historic(path);
        currentPlayer = historique.lastCoup().nextPlayer();
    }

    void play(Cell c){
        historique.play(c, currentPlayer, board);
        board.applyMove(c, currentPlayer);
        changePlayer();
    }

    int undo(){
        if (!historique.canUndo()){
            System.err.println("Impossible d'annuler le coup.");
            return 1;
        }
        Coup c = historique.undo();
        board = c.boardAvant;
        currentPlayer = c.nextPlayer();
        return 0;
    }

    void changePlayer(){
        currentPlayer =( currentPlayer+1 ) % 2;
    }

    int redo(){
        if (!historique.canRedo()){
            System.err.println("Impossible d'annuler le coup.");
            return 1;
        }
        Coup c = historique.redo();
        board = c.boardAvant;
        board.applyMove(c.cellule, c.joueur);
        currentPlayer = c.nextPlayer();
        return 0;
    }

    int saveGame(String path){
        return historique.saveToFile(path);
    }
}
