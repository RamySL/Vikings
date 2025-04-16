package server.model;

import java.awt.*;
import java.util.Random;

public class Warrior extends Viking {

    private static final float ATTACK_POWER = 20.0f; // Puissance d'attaque d'un guerrier
    private static final float DEFENSE_FACTOR = 0.5f; // Réduction des dégâts en défense

    public Warrior(float health, Point position, int camp) {
        super(health, position, camp);
    }

    /**
     * Attaque un camp ennemi.
     * - Inflige des dégâts aux guerriers ennemis.
     * - Réduit la force du camp ennemi.
     */
    public void attack(Camp camp_src, Camp targetCamp, int idRessource) {

        Field field = targetCamp.getFieldByID(idRessource);
        if(field.isPlanted()){
            camp_src.addRessource(field.getVegetable());
            targetCamp.removeRessource(field.getVegetable());
            field.setPlanted(false);
            field.setResource("");
        } else {
            System.out.println("Le champ " + field.getId() + " n'est pas planté !");
        }
    }

    public void moveAttack(Point destination, Camp campSrc, Camp camp, int idRessource) {
        new MovementThread(this, destination,campSrc, camp, true, idRessource).start();
    }
    /**
     * Défend le camp lorsqu'il est attaqué.
     * - Active la réduction des dégâts subis.
     */
    public void defend() {
        System.out.println("Le guerrier se défend !");
        this.takeDamage(ATTACK_POWER * DEFENSE_FACTOR); // Réduit les dégâts subis
    }

    /**
     * Applique des dégâts au guerrier.
     */
    public void damage(float amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
            System.out.println("Un guerrier est mort !");
        } else {
            System.out.println("Un guerrier a subi des dégâts, santé restante : " + this.health);
        }
    }

    @Override
    public String toString() {
        return "Warrior " + super.id;
    }

}
