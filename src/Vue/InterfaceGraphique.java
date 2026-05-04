package Vue;

import Modele.Board;
import Modele.Game;
import Patterns.Observateur;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import Control.ControleurMediateur;

public class InterfaceGraphique extends JComponent implements Runnable, InterfaceUtilisateur, Observateur{
    Game jeu;
    Dessin dessin;
    CollecteurEvenements control;
    PlayerPanel p1, p2;
    Action actionContainer;

    JFrame frame;
    JPanel root, gameContainer;

    public InterfaceGraphique(Game j, CollecteurEvenements c){
        jeu = j;
        control = c;
    }

    public static void demarrer(Game j, CollecteurEvenements c){
        InterfaceGraphique vue = new InterfaceGraphique(j, c);
        c.ajouteInterfaceUtilisateur(vue);
        ((ControleurMediateur) c).ajouterObservateur(vue);
        SwingUtilities.invokeLater(vue);
    }

    @Override
    public void run(){
        frame = new JFrame("CIRCLE OF LIFE ");
        frame.setSize(900, 600);
        dessin = new Dessin(jeu, p1, p2, control);
      
        gameContainer = new JPanel(new BorderLayout());
        gameContainer.setOpaque(false);
        gameContainer.add(dessin, BorderLayout.CENTER);

         p1 = new PlayerPanel(jeu,control,1);
         p2 = new PlayerPanel(jeu,control,2);
         actionContainer = new Action(jeu);

        root = new PanelRoot();

        root.add(p1, BorderLayout.EAST);
        root.add(gameContainer, BorderLayout.CENTER);
        root.add(p2,BorderLayout.WEST);
        root.add(actionContainer, BorderLayout.SOUTH);

    //    Timer timer = new Timer(30, e -> control.tictac());
    //    timer.start();
        frame.add(root);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    @Override
    public void miseAJour() {
        dessin.repaint();
        p1.update();
        p2.update();

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
