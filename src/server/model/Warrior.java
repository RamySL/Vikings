package server.model;

import java.awt.*;

public class Warrior extends Viking{

    public Warrior(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public void attack(){

    }

    public void defend(){

    }

    @Override
    public Point getPosition() {
        return this.position;
    }
}
