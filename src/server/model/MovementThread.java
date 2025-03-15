package server.model;

import java.awt.*;

class MovementThread extends Thread {
    private final Viking entity;
    private final Point destination;

    public MovementThread(Viking entity, Point destination) {
        this.entity = entity;
        this.destination = destination;
    }

    @Override
    public void run() {
        int destinationX = (entity.getCampId() == 1) ? destination.x - 400 : destination.x;
        int destinationY = destination.y;

        while (entity.getPosition().x != destinationX || entity.getPosition().y != destinationY) {
            if (entity.getPosition().x < destinationX) {
                entity.getPosition().x++;
            } else if (entity.getPosition().x > destinationX) {
                entity.getPosition().x--;
            }

            if (entity.getPosition().y < destinationY) {
                entity.getPosition().y++;
            } else if (entity.getPosition().y > destinationY) {
                entity.getPosition().y--;
            }try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}