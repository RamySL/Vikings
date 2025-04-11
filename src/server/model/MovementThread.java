package server.model;

import java.awt.*;

public class MovementThread extends Thread {
    private final Moveable entity;
    private Point destination;

    public MovementThread(Moveable entity, Point destination) {
        this.entity = entity;
        this.destination = destination;
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


    }

    public void stopMovement() {
        this.interrupt();
    }

}