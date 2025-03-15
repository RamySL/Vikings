package server.model;

import javax.swing.text.Position;
import java.awt.*;

public class Cow extends Livestock{

    public Cow(float health, Point position, int campId, int age/*, Camp camp*/) {
        super(health, position, campId, age/*, camp*/);
    }
/** j'ai mis dans livestock en supposant que les vache et mouton bougent
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
