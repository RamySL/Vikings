package server.model;

import java.awt.*;
import java.util.TreeSet;

/**
 * Detecte les collisions au sein des entité d'un même camp, arrête une des deux entités qui se déplace si collision
 */
public class ThreadCollisionCamp extends Thread {
    public static final int CHECK_INTERVAL_MS = 100;
    // le max que deux entité peuvent être proches
    public static final int CLOSEST = Position.WIDTH_VIKINGS - 5;
    private Camp camp;
    private TreeSet<Entity> entities;

    /**
     * Invariant gardé à l'init
     * @param camp
     */
    public ThreadCollisionCamp(Camp camp) {
        this.camp = camp;
        // on init l'ABR avec un comparateur qui compare les entités en fonction de leur distance (avec le (0,0))
        init();
    }

    public void init() {
        entities = new TreeSet<Entity>((e1, e2) -> {
            double distance1 = e1.getPosition().distance(new Point(0, 0));
            double distance2 = e2.getPosition().distance(new Point(0, 0));
            return Double.compare(distance1, distance2);
        });
        // on ajoute les entités à l'ABR
        entities.addAll(camp.getVikings());
        entities.addAll(camp.getLivestocks());
    }

    /**
     * Run method for the thread that checks for collisions between entities.
     */
    @Override
    public void run() {
        while (true) {
            checkCollisions();
            init();
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * on vérifie deux à deux les collisions entre les entités, on arrete les entités qui sont trop proches.
     */
    private void checkCollisions() {
        for (Entity entity1 : entities) {
            for (Entity entity2 : entities) {
                if (entity1 != entity2 && entity1.getPosition().distance(entity2.getPosition()) < CLOSEST) {
                    if(entity1 instanceof Moveable){
                        ((Moveable) entity1).stop();
                    } else if (entity2 instanceof Moveable) {
                        ((Moveable) entity2).stop();
                    }
                }
            }
        }

    }





}
