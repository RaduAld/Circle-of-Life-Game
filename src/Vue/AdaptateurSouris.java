package Vue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Modele.Cell;

public class AdaptateurSouris extends MouseAdapter{

    Dessin d;
    CollecteurEvenements control;

    public AdaptateurSouris(Dessin d, CollecteurEvenements c){
        this.d = d;
        this.control = c;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        Cell c = d.conversionPixelCell(e.getX(), e.getY());
        d.repaint();
        control.clicSouris(c);
    }
}