package Modele;

public class IAMedium extends IA{
    IAMedium(Game g){
        this.game = g;
        this.arbre = new ArbreEtOu(g.getCurrentPlayer());
        this.arbre.evaluate(this.arbre.BoardToString(game.getBoard()));
    }

    @Override
    Coup joue() {
        return null;
    }
}
