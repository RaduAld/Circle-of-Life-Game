package Vue;

import Modele.Board;


import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
       private final Color PLAYER_COLOR;
       JLabel joueur, nbrManger, nbrPoser;
       Board b;
       CollecteurEvenements control;
       int playernum;
       public PlayerPanel( Board b,CollecteurEvenements c,int num){
              setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
              this.b=b;
              this.control = c;
              this.playernum = num;
             if(playernum==1)
                    PLAYER_COLOR = Color.blue;
             else
                    PLAYER_COLOR = Color.red;
              joueur = Utilities.createJLabel("JOUEUR " + playernum,PLAYER_COLOR);
              joueur.setForeground(new Color(132, 141, 11));
              nbrManger = Utilities.createJLabel("Captures : 0/20",PLAYER_COLOR);
              nbrPoser =  Utilities.createJLabel("Jetons : 0",PLAYER_COLOR);
              add(joueur);
              add(Box.createVerticalStrut(20));
              add(nbrManger);
              add(Box.createVerticalStrut(5));
              add(nbrPoser);
              add(Box.createVerticalStrut(10));

           update();

       }
       void update(){
              nbrManger.setText("Captures : "+ b.getCaptured(playernum-1) +"/20");
              nbrPoser.setText("Jetons : "+b.getTokenCount(playernum-1));
              joueur.setForeground(PLAYER_COLOR);
       }

       @Override
       public void paintComponent(Graphics g){

       }

}
