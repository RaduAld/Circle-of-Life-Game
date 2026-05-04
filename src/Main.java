import Control.ControleurMediateur;
import Modele.*;
import Vue.*;

public class Main {

    public static void main(String[] args) {

        Game jeu = new Game(0);
        // Initialise le plateau - les tables statiques sont construites ici une seule fois
        ControleurMediateur  control =  new ControleurMediateur(jeu);
        InterfaceGraphique.demarrer(jeu, control);

        // Board board = jeu.getBoard();
        // System.out.println("=== Circle of Life - Exemple de partie ===\n");

        // // ------------------------------------------
        // // Tour 1 - Joueur 0 pose au centre (x=0, y=0, z=0)
        // // ------------------------------------------
        // System.out.println("Tour 1 - Joueur 0 pose au centre (x=0, y=0, z=0)");
        // jeu.play(new Cell(0, 0, 0));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 2 - Joueur 1 pose à droite du centre
        // // ------------------------------------------
        // System.out.println("Tour 2 - Joueur 1 pose en (x=1, y=0, z=-1)");
        // jeu.play(new Cell(1, 0, -1));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 3 - Joueur 0 étend son groupe vers le bas-droite
        // // ------------------------------------------
        // System.out.println("Tour 3 - Joueur 0 pose en (x=0, y=1, z=-1)");
        // jeu.play(new Cell(0, 1, -1));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 4 - Joueur 1 pose en haut-droite
        // // ------------------------------------------
        // System.out.println("Tour 4 - Joueur 1 pose en (x=1, y=-1, z=0)");
        // jeu.play(new Cell(1, -1, 0));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 5 - Joueur 0 pose à gauche du centre
        // // ------------------------------------------
        // System.out.println("Tour 5 - Joueur 0 pose en (x=-1, y=0, z=1)");
        // jeu.play(new Cell(-1, 0, 1));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 6 - Joueur 1 pose en haut-gauche du centre
        // // ------------------------------------------
        // System.out.println("Tour 6 - Joueur 1 pose en (x=0, y=-1, z=1)");
        // jeu.play(new Cell(0, -1, 1));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 7 - Joueur 0 complète son groupe en bas-gauche
        // // ------------------------------------------
        // System.out.println("Tour 7 - Joueur 0 pose en (x=-1, y=1, z=0)");
        // jeu.play(new Cell(-1, 1, 0));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Tour 8 - Joueur 1 pose plus loin à droite
        // // ------------------------------------------
        // System.out.println("Tour 8 - Joueur 1 pose en (x=2, y=0, z=-2)");
        // jeu.play(new Cell(2, 0, -2));
        // board = jeu.getBoard();
        // System.out.println(BoardUtils.boardToString(board));

        // // ------------------------------------------
        // // Affichage de l'état final
        // // ------------------------------------------
        // System.out.println("--- État final ---");
        // System.out.println("Jetons joueur 0 sur le plateau : " + board.getTokenCount(0));
        // System.out.println("Jetons joueur 1 sur le plateau : " + board.getTokenCount(1));
        // System.out.println("Captures par joueur 0 : "          + board.capturedByP0);
        // System.out.println("Captures par joueur 1 : "          + board.capturedByP1);
        // System.out.println("Cellules vides restantes : "       + BoardUtils.popCount(board.getEmptyMask()));

        // int winner = board.checkWinner(20);
        // if (winner == -1) System.out.println("Partie en cours - aucun vainqueur pour l'instant");
        // else              System.out.println("Vainqueur : Joueur " + winner);

        // // ------------------------------------------
        // // Accesseurs via notation humaine
        // // ------------------------------------------
        // System.out.println("\n--- Lecture via notation humaine (pas implémenté / utile pour le moment ) ---");
        // Cell cellE4 = BoardUtils.fromNotation("E4");
        // System.out.println("Cellule E4           = " + cellE4);
        // // - spiralIndexOf et neighboursOf sont maintenant sur Board, pas sur Cell
        // System.out.println("Indice spiral de E4  = " + board.spiralIndexOf(cellE4));
        // System.out.println("Anneau de E4         = " + cellE4.ring());
        // System.out.println("Occupant de E4       = " + board.getOwner(cellE4));

        // // - Voisins du centre via Board.neighboursOf()
        // System.out.println("\nVoisins du centre (x=0, y=0, z=0) :");
        // Cell centre = new Cell(0, 0, 0);
        // for (Cell voisin : board.neighboursOf(centre)) {
        //     System.out.println("  " + voisin + "  indice=" + board.spiralIndexOf(voisin)
        //             + "  occupant=" + board.getOwner(voisin));
        // }

        // // ------------------------------------------
        // // Groupes connectés du joueur 0
        // // ------------------------------------------
        // System.out.println("\n--- Groupes connectés du joueur 0 ---");
        // var groups = board.getAllGroups(0);
        // for (int i = 0; i < groups.size(); i++) {
        //     var cells = board.groupToCells(groups.get(i));
        //     System.out.print("Groupe " + i + " (" + cells.size() + " cellule(s)) : ");
        //     for (Cell c : cells) System.out.print(c.toNotation() + " ");
        //     System.out.println();
        // }

        // // ------------------------------------------
        // // Identification des espèces présentes sur le plateau
        // // ------------------------------------------
        // System.out.println("\n--- Espèces identifiées ---");
        // for (int p = 0; p <= 1; p++) {
        //     System.out.println("Joueur " + p + " :");
        //     for (var group : board.getAllGroups(p)) {
        //         var cells        = board.groupToCells(group);
        //         Critter critter  = CritterRegistry.identify(cells);
        //         String specName  = critter != null ? critter.name + " (rang " + critter.rank + ")" : "inconnue";
        //         System.out.print("  " + specName + " → cellules : ");
        //         for (Cell c : cells) System.out.print(c.toNotation() + " ");
        //         if (critter != null) {
        //             Critter prey = CritterRegistry.preyOf(critter);
        //             System.out.print(" | mange : " + prey.name);
        //         }
        //         System.out.println();
        //     }
        // }

        // // Vérifie la chaîne alimentaire complète
        // System.out.println("\n--- Chaîne alimentaire complète ---");
        // for (Critter s : CritterRegistry.ALL) {
        //     Critter prey = CritterRegistry.preyOf(s);
        //     System.out.printf("  rang %2d  %-12s  mange  %s%n", s.rank, s.name, prey.name);
        // }

        // // ------------------------------------------
        // // Support solveur
        // // ------------------------------------------
        // System.out.println("\n--- Support solveur ---");
        // System.out.println("Hash Zobrist    : " + board.zobristHash());
        // Board copy = board.copy();
        // System.out.println("Copie identique : " + board.equals(copy));
        // System.out.println("Coups légaux    : " + board.hasLegalMoves());
    }
}