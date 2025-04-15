package server.model;

import server.model.GameTimer;

import java.util.ArrayList;

/**
 * Partie de jeu avec les camps
 */

public class Partie {
    private Camp[] camps;
    private GameTimer timer;


    public Partie(Camp[] camps, GameTimer timer) {
        this.camps = camps;
        this.timer = timer;
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

    public boolean isGameOver() {
        if (timer.getRemainingTime() <= 0) {
            return true;
        }

        return false;
    }
    public int getRemainingTime() {
        return timer.getRemainingTime();
    }


}