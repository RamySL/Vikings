package model;

import java.util.ArrayList;

/**
 * Classe qui va regroupé toutes les donnée modèle de la partie
 */
public class Partie {
    private ArrayList<Mouton> moutons;

    public Partie() {
        this.moutons = new ArrayList<>();
    }

    // add a sheep to the list of sheeps
    public void addMouton(Mouton mouton) {
        moutons.add(mouton);
    }

    public ArrayList<Mouton> getMoutons() {
        return moutons;
    }
}
