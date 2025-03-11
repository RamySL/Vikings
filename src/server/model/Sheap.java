package server.model;

import javax.swing.text.Position;
import java.awt.*;

public class Sheap extends Livestock{


    public Sheap(float health, Point position, int camp) {
        super(health, position, camp);
    }

    @Override
    public void move(Point destination) {
        this.position = destination;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void sex() {

    }
}
