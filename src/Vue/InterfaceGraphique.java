package Vue;

import Modele.Board;
import Patterns.Observateur;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Control.ControleurMediateur;

public class InterfaceGraphique extends JComponent implements Runnable, InterfaceUtilisateur, Observateur{
    Board board;
    Dessin dessin;

    CollecteurEvenements control;
    JFrame frame;
    JPanel root, gameContainer;

    public InterfaceGraphique(Board b, CollecteurEvenements c){
        board = b;
        control = c;
    }

    public static void demarrer(Board b, CollecteurEvenements c){
        InterfaceGraphique vue = new InterfaceGraphique(b, c);
        c.ajouteInterfaceUtilisateur(vue);
        SwingUtilities.invokeLater(vue);
    }

    @Override
    public void run(){
        frame = new JFrame("CIRCLE OF LIFE ");
        frame.setSize(900, 500);
        dessin = new Dessin(board, control);
        gameContainer = new JPanel(new BorderLayout());
        gameContainer.setOpaque(false);
        gameContainer.add(dessin, BorderLayout.CENTER);

        PlayerPanel p1 = new PlayerPanel(board,control,1);
        PlayerPanel p2 = new PlayerPanel(board,control,2);
        Action actionContainer = new Action(board);

        root = new PanelRoot();

        root.add(p1, BorderLayout.EAST);
        root.add(gameContainer, BorderLayout.CENTER);
        root.add(p2,BorderLayout.WEST);
        root.add(actionContainer, BorderLayout.SOUTH);

      

        board.ajouterObservateur(this);

    //    Timer timer = new Timer(30, e -> control.tictac());
    //    timer.start();
        frame.add(root);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    @Override
    public void miseAJour() {
        dessin.repaint();


//        if (Board.BoardTermine()) {
//            defaite(joueurEnCours);
//        }
    }

//    public void defaite(int joueurGagnant) {
//        gameContainer.remove(dessin);
//
//        gameOver = createJLabel("Game Over - Joueur " + joueurGagnant + " gagne !");
//        gameOver.setForeground(new Color(224, 133, 57));
//        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
//
//        gameContainer.add(gameOver, BorderLayout.CENTER);
//
//        annuler.setEnabled(false);
//        rejouer.setEnabled(false);
//        sauvegarder.setEnabled(false);
//        restaurer.setEnabled(false);
//        nouvellePartie.setEnabled(true);
//
//        gameContainer.revalidate();
//        gameContainer.repaint();
//    }
//}{
}
