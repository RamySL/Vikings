package server.model;

import java.awt.*;

/*
    Blé
 */
public class Wheat extends Vegetable{
    public Wheat(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public void grow(){
        System.out.println("Wheat is growing");
    }
}
