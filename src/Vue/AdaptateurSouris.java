package Vue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Modele.Cell;

public class AdaptateurSouris extends MouseAdapter{

    Dessin d;
    CollecteurEvenements control;
    PlayerPanel p1, p2;

    public AdaptateurSouris(Dessin d, CollecteurEvenements c, PlayerPanel p1, PlayerPanel p2){
        this.d = d;
        this.control = c;
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void mousePressed(MouseEvent e){
        Cell c = d.conversionPixelCell(e.getX(), e.getY());
        d.repaint();
        control.clicSouris(c);
    }
}