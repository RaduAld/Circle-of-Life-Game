package Modele;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ArbreEtOu {
    HashMap<String, Boolean> arbre = new HashMap<>();
    int player;

    ArbreEtOu(int p){
        this.player = p;
    }

    String BoardToString(Board config){
        String vecteur = "";
        vecteur += config.p0.toString() + "_" + config.p0.toString() + "_" + Integer.toString(config.capturedByP0) + "_" + Integer.toString(config.capturedByP1);
        return vecteur;
    }
    Board StringToBoard(String vecteur){
        return new Board(vecteur.split("_"));
    }

    boolean hasPossibleMoves(String newConfig){
        Board board = StringToBoard(newConfig);
        return board.hasLegalMoves();
    }
    boolean checkLosing(int captureThreshold, String newConfig){
        Board board = StringToBoard(newConfig);
        return board.checkLosingByPlayer(captureThreshold, this.player);
    }

    List<String> get_children (String config){
        List<String> children = new ArrayList<>();
        Board board = StringToBoard(config);
        for(int i=0; i<Board.CELL_COUNT; i++){
            Board child = board.applyMove(Board.spiralCells[i], this.player);
            if (child != null){
                children.add(BoardToString(child));
            }
        }
        return children;
    }

    boolean evaluate(String newConfig){
        // if configuration already exists
        for (String configuration : this.arbre.keySet()) {
            if (configuration.equals(newConfig)){
                return this.arbre.get(configuration);
            }
        }
        //if we are in a losing config, that means that the previous player played that move (manger 0,0), so it's actually a winning move from the ai's perspective
        if (!hasPossibleMoves(newConfig) || checkLosing(20, newConfig)){
            //changing from false to true
            arbre.put(newConfig, true);
            return true;
        }
        // generate all subtrees and evaluate them
        List<String> possible_coupes = get_children(newConfig);
        for (String configuration : possible_coupes){
            boolean outcome = evaluate(configuration);
            // if exists winning outcome, we return true
            if (!outcome){
                arbre.put(newConfig, true);
                return true;
            }
        }
        // if all outcomes of subtrees losing, return false
        arbre.put(newConfig, false);
        return false;
    }
}
