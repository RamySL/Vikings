package server.model;

import java.awt.*;

public class Farmer extends Viking{

    public Farmer(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public void plant(){

    }

    public void harvest(){

    }

    public void feed(){

    }

    public void water(){

    }

    @Override
    public Point getPosition() {
        return this.position;
    }
}
