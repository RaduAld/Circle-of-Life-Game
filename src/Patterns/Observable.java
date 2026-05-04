package Patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Observable {
    List<Observateur> ob = new ArrayList<>();

    public void ajouterObservateur(Observateur o){
        ob.add(o);
    }

    public void notifierObservateur(){
        for(Observateur o : ob)
            o.miseAJour();
    }

}
