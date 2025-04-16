package server.model;

import java.awt.*;

public class MovementThread extends Thread {
    private final Moveable entity;
    private Point destination;
    private boolean attack;
    private Camp enemy;
    private int idRessource;
    private Camp campSrc;

    public MovementThread(Moveable entity, Point destination) {
        this.entity = entity;
        this.destination = destination;
        this.attack = false;
    }

    public MovementThread(Moveable entity, Point destination,Camp campSrc,  Camp camp, boolean attack, int idRessource) {
        this.entity = entity;
        this.destination = destination;
        this.attack = attack;
        this.enemy = camp;
        this.idRessource = idRessource;
        this.campSrc = campSrc;

    }

    @Override
    public void run() {
            while ((entity.getPosition().x != destination.x || entity.getPosition().y != destination.y)) {
                if (entity.getPosition().x < destination.x) {
                    entity.getPosition().x++;
                } else if (entity.getPosition().x > destination.x) {
                    entity.getPosition().x--;
                }

                if (entity.getPosition().y < destination.y) {
                    entity.getPosition().y++;
                } else if (entity.getPosition().y > destination.y) {
                    entity.getPosition().y--;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(attack){
                attack((Warrior) entity, campSrc, enemy, idRessource);
                attack = false;
            }


    }

    public void attack(Warrior warrior, Camp campSrc, Camp enemy, int idRessource) {
        warrior.attack(campSrc, enemy, idRessource);
    }

    public void stopMovement() {
        this.interrupt();
    }

}