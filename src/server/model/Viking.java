package server.model;

import java.awt.*;


public abstract class Viking extends Entity implements Moveable {

    protected static float coeffStrength = 1.0f; // Coefficient appliqué à tous les Vikings
    protected transient MovementThread currentMovementThread = null; // Thread de mouvement actuel

    public Viking(float health, Point position, int campId) {
        super(health, position, campId);
    }

    @Override
    public void move(Point destination) {
        currentMovementThread =  new MovementThread(this, destination);
        currentMovementThread.start();
    }

    @Override
    public void stop() {
        if (currentMovementThread != null) {
            currentMovementThread.stopMovement();
            currentMovementThread = null;
        }
    }

    /**
     * Mange un animal et augmente la force du camp.
     * La vérification de proximité est gérée par un thread extérieur.
     * La force gagnée est égale à la santé de l'animal multipliée par le coeffStrength global des Vikings.
     */

    public void eat(Livestock animal, Camp camp) {
        // Calcul de la force gagnée (santé * coeffStrength partagé par tous les Vikings)
        float strengthGained = animal.getHealth() * coeffStrength;
        camp.vikingEats(animal);
        System.out.println("Force du camp augmentée de " + strengthGained);
    }


    /**
     * Définit le coefficient de force global pour tous les Vikings.
     * @param coeff Nouveau coefficient.
     */
    public static void setCoeffStrength(float coeff) {
        coeffStrength = coeff;
    }

    /**
     * Récupère le coefficient de force global des Vikings.
     * @return coeffStrength
     */
    public static float getCoeffStrength() {
        return coeffStrength;
    }

    /**
     * Applique des dégâts au Viking.
     */
    public void takeDamage(float amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Vérifie si le Viking est encore en vie.
     * @return true si le Viking a encore de la vie.
     */
    public boolean isAlive() {
        return this.health > 0;
    }
}
