package server.model;

import javax.swing.text.Position;
import java.awt.*;

public abstract class Viking extends Entity implements Moveable{


    public Viking(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public void move(Point destination) {
        new MovementThread(this, destination).start();
    }
    public void eat(){

    }
}
