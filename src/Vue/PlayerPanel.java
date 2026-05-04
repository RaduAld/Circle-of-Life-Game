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
              setOpaque(false);
              this.b=b;
              this.control = c;
              this.playernum = num;
             if(playernum==1)
                    PLAYER_COLOR = Color.red;
             else
                    PLAYER_COLOR = Color.blue;
             
              joueur = Utilities.createJLabel("JOUEUR " + playernum,PLAYER_COLOR);
              nbrManger = Utilities.createJLabel("Captures : 0/20",PLAYER_COLOR);
              nbrPoser =  Utilities.createJLabel("Jetons : 0",PLAYER_COLOR);
  
              JPanel infoPanel = new JPanel();
              infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
              infoPanel.setBackground(new Color(255, 255, 255, 180)); // blanc semi-transparent
              infoPanel.add(joueur);
              add(Box.createVerticalStrut(20));
              infoPanel.add(nbrManger);
              add(Box.createVerticalStrut(5));
              infoPanel.add(nbrPoser);
              add(infoPanel);

              update();

       }
       void update(){
              Board b = control.getBoard(); 
              nbrManger.setText("Captures : "+ b.getCaptured(playernum-1) +"/20");
              nbrPoser.setText("Jetons : "+b.getTokenCount(playernum-1));
              joueur.setForeground(PLAYER_COLOR);
              repaint();
       }

}
