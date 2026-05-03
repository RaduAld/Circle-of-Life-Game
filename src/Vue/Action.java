package Vue;

import Vue.Utilities;
import Modele.Board;

import javax.swing.*;
import java.awt.*;

public class Action extends JPanel{

    JButton Annuler, Abandonner, Confirmer ;
    Board b;

    public Action(Board b){
        this.b =b;
        Annuler = Utilities.createJButton("Annuler");
        Abandonner = Utilities.createJButton("Abandonner");
        Confirmer = Utilities.createJButton("Confirmer");

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Annuler);
        add(Box.createHorizontalGlue());
        add(Confirmer);
        add(Box.createHorizontalGlue());
        add(Abandonner);

    }

    @Override
public void paintComponent(Graphics g){

}
}
