package Modele;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Historic {
    private Stack<Coup> before;
    private Stack<Coup> after;

    public Historic(){
        before = new Stack<>();
        after = new Stack<>();
    }

    //Initialise l'historique depuis un fichier
    public Historic(String path) {
        before = new Stack<>();
        after = new Stack<>();
        Board board = new Board();

        try (InputStream in = new FileInputStream(path)) {
            int count = in.read();

            for (int i = 0; i < count; i++) {
                int id    = in.read() & 0xFF;  // masque pour éviter le signe
                int joueur = in.read() & 0xFF;
                Cell cell  = Board.spiralCells[id];
                before.push(new Coup(cell, joueur, board)); // board avant
                board = board.applyMove(cell, joueur);// board après avoir joué le coup
            }
            in.close();
        }
        catch(IOException e){
            System.out.println("Erreur lors de la lecture depuis le fichier.");
        }
    }

    public void play(Cell cellule, int joueur, Board boardActuel) {
        before.push(new Coup(cellule, joueur, boardActuel));
        after.clear();
    }

    //Retourne le coup annulé
    public Coup undo() {
        if (before.isEmpty())
            return null;
        Coup c = before.pop();
        after.push(c);
        return c;
    }

    //Retourne le dernier coup rejoué
    public Coup redo() {
        if (after.isEmpty())
            return null;
        Coup c = after.pop();
        before.push(c);
        return c;
    }

    public boolean canUndo() {
        return !before.isEmpty();
    }
    public boolean canRedo() {
        return !after.isEmpty();
    }


    public int saveToFile(String path){
        try (OutputStream out = new FileOutputStream(path)) {
            List<Coup> coups = new ArrayList<>(before);
            out.write(coups.size());

            for (Coup c : coups) {
                int id = Board.cellToSpiral.get(c.cellule);
                out.write(id);       // indice spiral : 0-60
                out.write(c.joueur);  // joueur : 0 ou 1
            }
            out.close();
            return 0;
        }
        catch(IOException e){
            System.out.println("Erreur lors de la sauvegarde dans le fichier.");
            return 1;
        }
    }

    public Coup lastCoup(){
        if (before.isEmpty()){
            return null;
        }
        else return before.peek();
    }
}