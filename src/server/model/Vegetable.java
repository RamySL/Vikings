package server.model;

import java.awt.*;

public abstract class Vegetable extends Entity {
    protected int growthStage; // Stade de croissance actuel
    protected int maxGrowthStage; // Stade de croissance maximal avant maturité

    public Vegetable(float health, Point position, int camp, int maxGrowthStage) {
        super(health, position, camp);
        this.growthStage = 0;
        this.maxGrowthStage = maxGrowthStage;
    }

    /**
     * Augmente la croissance du légume.
     */
    public void grow() {
        if (growthStage < maxGrowthStage) {
            growthStage++;
            System.out.println("Le légume grandit, stade actuel : " + growthStage);
        } else {
            System.out.println("Le légume est déjà mature !");
        }
    }

    /**
     * Vérifie si le légume est mature et peut être récolté.
     */
    public boolean isMature() {
        return growthStage >= maxGrowthStage;
    }

    /**
     * Retourne le stade de croissance actuel.
     */
    public int getGrowthStage() {
        return growthStage;
    }
}
