package server.model;

import javax.swing.text.Position;
import java.awt.*;

public class Cow extends Livestock{

    public Cow(float health, Point position, int campId, int age) {
        super(health, position, campId, age);
    }

    @Override
    public String toString() {
        return "Cow " + super.id;
    }


  

}
