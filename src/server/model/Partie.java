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

    // gtter en foncion de l'id
    public Camp getCamp(int id) {
        for (Camp camp : camps) {
            if (camp.getId() == id) {
                return camp;
            }
        }
        return null;
    }

}