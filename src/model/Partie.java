package model;

/**
 * Classe qui va regroupé toutes les donnée modèle de la partie
 */
public class Partie {
    private Mouton mouton;

    public Partie() {
        this.mouton = new Mouton(1, 100, 100);
    }

    public Mouton getMouton() {
        return mouton;
    }


}
