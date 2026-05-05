package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IADifficile extends IA{
    int MAX_TREE_DEPTH = 5;
    public IADifficile(Game g){
        //remember
        this.game = g;
        this.AIplayer = g.getCurrentPlayer();
        this.arbre = new ArbreEtOu(AIplayer);
        // this.arbre.evaluate(this.arbre.BoardToString(game.getBoard()));
    }

    @Override
    public Coup joue(){
        String avantBoard = arbre.BoardToString(game.getBoard());
        List<String> children = arbre.get_children(avantBoard);
        List<String> winning_children = new ArrayList<>();
        for(String child : children){
            if (!arbre.evaluate(child, MAX_TREE_DEPTH)) {
                winning_children.add(child);
            }
        }
        Random r = new Random();
        Cell cellule = null;
        if (!winning_children.isEmpty()) {
            cellule = Cell.getCellule(avantBoard, winning_children.get(r.nextInt(winning_children.size())), AIplayer);
        }else{
            cellule = Cell.getCellule(avantBoard, children.get(r.nextInt(children.size())), AIplayer);
        }
        if (cellule == null){
            System.err.println("Error in getting the cellule");
            return null;
        }
        return new Coup(cellule, AIplayer, game.getBoard());
    }
}
