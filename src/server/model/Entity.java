package server.model;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Entity {

    protected float health;
    // center of the entity
    protected Point position;
    protected int campId;

    public Entity(float health, Point position, int campId) {
        this.health = health;
        this.position = position;
        this.campId = campId;
    }

    public int getCampId() {
        return campId;
    }
    public float getHealth() {
        return health;
    }
    public Point getPosition() {
        return position;
    }
    public boolean isAlive() {
        return this.health > 0;
    }
}
