package Vue;

import Modele.Board;
import Modele.Cell;

public interface CollecteurEvenements {
    void clicSouris(Cell c);
    Board getBoard();
    //void toucheClavier(String t);
    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue);
    //void tictac();
}
