package Modele.Tests;
import Modele.Board;
import Modele.Cell;
import Modele.Historic;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestHistorique {
    @Test
    public void testJouer(){
        Historic historique = new Historic();
        Board board = new Board();

        historique.play(new Cell(0, 0, 0), 0, board);
        board = board.applyMove(new Cell(0, 0, 0), 0);

        historique.play(new Cell(1, 0, -1), 0, board);
        board = board.applyMove(new Cell(1, 0, -1), 1);

        assertEquals(historique.canUndo(), true);
        assertEquals(historique.canRedo(), false);
    }

    @Test
    public void testaAnnuler(){
        Historic historique = new Historic();
        Board board = new Board();

        historique.play(new Cell(0, 0, 0), 0, board);
        Board boardTamp = board.applyMove(new Cell(0, 0, 0), 0);


        historique.play(new Cell(1, 0, -1), 0, boardTamp);
        board = boardTamp.applyMove(new Cell(1, 0, -1), 1);

        board = historique.undo().boardAvant;
        assertEquals(board, boardTamp);
    }

    @Test
    public void testaRefaire(){
        Historic historique = new Historic();
        Board board = new Board();

        historique.play(new Cell(0, 0, 0), 0, board);
        Board lastBoard = board.applyMove(new Cell(0, 0, 0), 0);

        historique.play(new Cell(1, 0, -1), 0, lastBoard);
        board = lastBoard.applyMove(new Cell(1, 0, -1), 1);

        board = historique.undo().boardAvant;
        board = historique.redo().boardAvant;
        assertEquals(board, lastBoard);

    }

    @Test
    public void testSave(){
        Historic historique = new Historic();
        Board board = new Board();

        historique.play(new Cell(0, 0, 0), 0, board);
        board = board.applyMove(new Cell(0, 0, 0), 0);

        historique.play(new Cell(1, 0, -1), 0, board);
        board = board.applyMove(new Cell(1, 0, -1), 1);

        historique.saveToFile("src/Modele/Tests/savingFile.txt");
        Historic newHistoric = new Historic("src/Modele/Tests/savingFile.txt");

        assertEquals(newHistoric.lastCoup().boardAvant, historique.lastCoup().boardAvant);

    }

}
