package server.model;

import javax.swing.text.Position;
import java.awt.*;

public interface Moveable {

    public void move(Point destination);
    public Point getPosition();

}
