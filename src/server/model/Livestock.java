package server.model;

import java.awt.*;

/**
 * Bétail
 */
public abstract class Livestock extends Entity implements Moveable {
    protected int age;
    protected boolean isHealthy;
    protected boolean isPregnant = false;
    protected int gestationTime = 5000; // 5 secondes de gestation (simulation)
    protected transient MovementThread currentMovementThread = null; // Thread de mouvement actuel


    public Livestock(float health, Point position, int campId, int age) {
        super(health, position, campId);
        this.age = age;
        this.isHealthy = true;
    }
    public void move(Point destination) {
        currentMovementThread =  new MovementThread(this, destination);
        currentMovementThread.start();
    }

    @Override
    public void stop() {
        if (currentMovementThread != null) {
            currentMovementThread.stopMovement();
        }
    }

    /*public void accouche() {
        if (!isPregnant) {
            isPregnant = true;
            System.out.println("Une gestation commence...");

            new Thread(() -> {
                try {
                    Thread.sleep(gestationTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Livestock baby = this instanceof Cow ? new Cow(100, this.position, this.campId, 0/*, camp*//*) :
                                new Sheep(100, this.position, this.campId,0/*,camp*//*);

                /*camp.addLivestock(baby);
                isPregnant = false;
                System.out.println("Un bébé est né !");
            }).start();
        } else {
            System.out.println("Déjà en gestation.");
        }
    }*/
}
