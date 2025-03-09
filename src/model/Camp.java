package model;

/**
 * Représente un camp viking, avec un nombre de guerriers, bétail, nourriture, etc.
 */
public class Camp {

    private Mouton mouton;

    // creer un camp avec un mouton placé aleatoirement
    public Camp() {
        this.mouton = new Mouton(1,100,100);

    }


}
