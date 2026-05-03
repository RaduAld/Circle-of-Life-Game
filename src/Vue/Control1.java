package Vue;

import Vue.Utilities;
import Modele.Board;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Control1 extends JPanel {
       public static final Color PLAYER1_COLOR = new Color(202, 50, 3);
       JLabel joueur, nbrManger1, nbrPoser1;
       Board b;

       public Control1( Board b){
              setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
              this.b=b;

              joueur = Utilities.createJLabel("JOUEUR 1",PLAYER1_COLOR);
              nbrManger1 = Utilities.createJLabel("Captures : 0/20",PLAYER1_COLOR);
              nbrPoser1 =  Utilities.createJLabel("Jetons : 0",PLAYER1_COLOR);

              add(joueur);
              add(Box.createVerticalStrut(20));
              add(nbrManger1);
              add(Box.createVerticalStrut(5));
              add(nbrPoser1);
              add(Box.createVerticalStrut(10));

           update();

       }

       void update(){
              nbrManger1.setText("Captures : "+ b.capturedByP0 +"/20");
              nbrPoser1.setText("Jetons : "+b.getTokenCount(1));
       }

       @Override
       public void paintComponent(Graphics g){

       }

}
