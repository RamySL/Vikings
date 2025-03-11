package server.model;

import java.awt.*;

public abstract  class Vegetable extends Entity{

    public Vegetable(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public abstract void grow();
}
