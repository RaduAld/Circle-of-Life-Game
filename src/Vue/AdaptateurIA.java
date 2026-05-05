package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurIA implements ActionListener {
    CollecteurEvenements control;
    public AdaptateurIA(CollecteurEvenements c){
        control = c;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        control.toucheClavier("IA");
    }
}
