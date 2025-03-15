package server.model;

import java.awt.*;

/*
    Blé (Wheat) - Hérite de Vegetable
 */
public class Wheat extends Vegetable {

    public Wheat(float health, Point position, int camp, int growthStage) {
        super(health, position, camp, 5); // 5 = maxGrowthStage
        this.growthStage = growthStage;
    }
}

