/*package server.model;

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
        int destinationX = destination.x;
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
}*/package server.model;

import java.awt.*;

public class MovementThread extends Thread {
    private final Viking entity;
    private Point destination;
    private volatile boolean running = true;

    public MovementThread(Viking entity, Point destination) {
        this.entity = entity;
        this.destination = destination;
    }

    @Override
    public void run() {
        while (running) {
            int destinationX = destination.x;
            int destinationY = destination.y;

            while (running && (entity.getPosition().x != destinationX || entity.getPosition().y != destinationY)) {
                if (entity.getPosition().x < destinationX) {
                    entity.getPosition().x++;
                } else if (entity.getPosition().x > destinationX) {
                    entity.getPosition().x--;
                }

                if (entity.getPosition().y < destinationY) {
                    entity.getPosition().y++;
                } else if (entity.getPosition().y > destinationY) {
                    entity.getPosition().y--;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // Exit the loop if the thread is interrupted
                    running = false;
                    break;
                }
            }
        }
    }

    public void stopMovement() {
        running = false;
        this.interrupt();
    }

    public void updateDestination(Point newDestination) {
        this.destination = newDestination;
        if (!this.isAlive()) {
            this.running = true;
            this.start();
        }
    }
}