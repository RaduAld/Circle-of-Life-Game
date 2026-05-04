package Vue;

import Modele.Board;
import Modele.Game;
import Modele.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dessin extends JComponent {
    Game jeu;
   // taille du centre jusqua un sommet
   public static final double taille_case = 30.0;
    CollecteurEvenements control;
    PlayerPanel p1, p2;

    public Dessin( Game j, PlayerPanel p1, PlayerPanel p2, CollecteurEvenements c){
        jeu = j;
        control = c;
        this.p1 = p1;
        this.p2 = p2;

        addMouseListener(new AdaptateurSouris(this, control,p1, p2));
    }

    //creer un hexgone : 6 sommets espacés de 60 degré,
    //permet de creer les six points de l'hexagon a relier apres
    //prend le centre de lhexag cx et cy 'coordonnées en pixel'
    Polygon trouverSommetsHex(double cx, double cy){
        Polygon p = new Polygon();
        double taille_affichage = taille_case * 0.92; //pour de lespace entre les hex
        for(int i=0; i<6; i++){
           double angle = Math.toRadians(60*i + 30);
           int a = (int) (cx + taille_affichage*Math.cos(angle));
           int b = (int) (cy + taille_affichage*Math.sin(angle));

           p.addPoint(a,b);
        }
        return p;
    }

    //fct pour trouver le centre du cell en pixel
    public  double[] centrePixel(Cell c){
        int cx = getWidth()/2;
        int cy = getHeight()/2;

        //pour passer d’un centre d’hexagone à l’autre horizontalement il faut multiplier par racine de 3 
        //la largeur de gauche vers droite vaut 2*cos(30) = v3
        //c.z / 2 pour prendre en compte le décalage horizontal entre les lignes dhexag sinon on aura une sorte de carre dhexag
        int x = cx + (int)((c.x + c.y / 2.0) * taille_case * Math.sqrt(3));
        
        //la coord verticale 
        //les hexagones se chauvauchesnt donc entre deux centres verticalement
        //on a pas taille_case*2 mais taille_case*1.5 pour prendre en compte le chevauchement
        //y = cy + c.z * (3/2) * taille_case
        int y = cy + (int)(c.y * (taille_case + taille_case/2));

        return new double[]{x,y};
    }

    
//utile pour ladaptateur souris, pour savoir quelle case il a coché depuis les cordonnées pixel de la souris
    public Cell conversionPixelCell(int mx, int my) {
        double cx = getWidth() / 2.0;
        double cy = getHeight() / 2.0;
        
        //pour quon travaille selon le centre de la fenetre car cest aussi le centre de la grille cest a cx cy quon a la case 0, 0
        double dx = mx - cx;
        double dy = my - cy;
 
        // on fait linverse de ce quon a fait quand on la creer 
          
        //quand on  fait celle vers pixel :  y = cy + c.z * (3/2) * taille_case    ->  y = cy + dy on cherche cz  
        //dy = c.z * (3/2) * taille_case
       // du coup c.z = dy/ (3/2*t)  voili voilou
        double y = (2.0/3.0 * dy) / taille_case;
     
        //x=cx+dx mm que y    dx = (c.x + c.z / 2.0) * taille_case * sqrt(3);
       // dx / (sqrt(3)*taille_case) = c.x + c.z/2
       //
        double x = dx/(Math.sqrt(3)*taille_case) - y/2;
    
        double z = -x - y;
 
        // arrondi cube (conserve x+y+z=0) pour garder ue des valuers entieres
        int rx = (int)Math.round(x), ry = (int)Math.round(y), rz = (int)Math.round(z);
 
        Cell c = new Cell(rx, ry, rz);
        return Board.cellToSpiral.containsKey(c) ? c : null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Board board = jeu.returnBoard();
        for (int i=0; i<board.cellCount(); i++){
            Cell cell = board.getCell(i);
            drawHex(g2, cell, board);
        }
    }

    public  void drawHex(Graphics2D g, Cell c,Board board){

        double[] d = centrePixel(c);
        Polygon hex = trouverSommetsHex(d[0], d[1]);
        int owner = board.getOwner(c);
        if (owner == 0) {
            g.setColor(new Color(193, 109, 83));
        } else if (owner == 1) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(new Color(207, 185, 106, 220));
        }
        //g.setColor(new Color(207, 185, 106, 220));
        g.fillPolygon(hex);
       
        g.setColor(new Color(80, 50, 20));
        g.drawPolygon(hex);

    }


}
