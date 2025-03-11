package server.model;

import java.awt.*;

/**
 * Bétail
 */
public abstract class Livestock extends Entity implements Moveable {

    public Livestock(float health, Point position, int camp) {
        super(health, position, camp);
    }
    public abstract void sex();
}
