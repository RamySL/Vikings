package server.model;

import javax.swing.text.Position;
import java.awt.*;

public class Sheap extends Livestock{


    public Sheap(float health, Point position, int campId, int age) {
        super(health, position, campId, age);
    }


/**pareil que pour cow 
    @Override
    public void move(Point destination) {
        this.position = destination;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }
    */

    
}
