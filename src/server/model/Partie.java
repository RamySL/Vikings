package server.model;

import java.util.ArrayList;

/**
 * Partie de jeu avec les camps
 */

public class Partie {
    private Camp[] camps;


    public Partie(Camp[] camps ) {
        this.camps = camps;
    }

    public Camp[] getCamps() {
        return camps;
    }

}