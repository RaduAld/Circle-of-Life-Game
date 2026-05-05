package Modele;

import Global.Configuration;

import java.util.ArrayList;

public abstract class IA {
    Game game;
    ArbreEtOu arbre;
    int AIplayer;

    public Coup elaboreCoup() {
        return joue();
    }

    Coup joue() {
        return null;
    }
}

