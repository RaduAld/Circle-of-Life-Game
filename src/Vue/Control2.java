package Vue;

import Vue.Utilities;
import Modele.Board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Control2 extends JPanel {
        public static final Color PLAYER2_COLOR = new Color(80, 80, 200);
        JLabel joueur, nbrManger2, nbrPoser2;
        Board b;

       public Control2( Board b){
              setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
              this.b=b;

              joueur = Utilities.createJLabel("JOUEUR 2", PLAYER2_COLOR);
              nbrManger2 = Utilities.createJLabel("Captures : 0/20", PLAYER2_COLOR);
              nbrPoser2 =  Utilities.createJLabel("Jetons : 0", PLAYER2_COLOR);

              add(joueur);
              add(Box.createVerticalStrut(20));
              add(nbrManger2);
           add(Box.createVerticalStrut(5));
              add(nbrPoser2);
              add(Box.createVerticalStrut(20));

           update();

       }

       void update(){
             for(int i=2; i< 3; i++){
              nbrManger2.setText("Captures : "+ b.capturedByP0 +"/20");
              nbrPoser2.setText("Jetons : "+b.getTokenCount(2));

             }

       }

       @Override
       public void paintComponent(Graphics g){

       }

}
