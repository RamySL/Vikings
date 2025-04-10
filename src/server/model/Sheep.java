package server.model;

import java.awt.*;

public class Sheep extends Livestock{


    public Sheep(float health, Point position, int campId, int age) {
        super(health, position, campId, age);
    }

    // String avec nom de la classe
    @Override
    public String toString() {
        return "Sheep";
    }





}
