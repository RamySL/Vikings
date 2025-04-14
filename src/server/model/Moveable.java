package server.model;

import javax.swing.text.Position;
import java.awt.*;

public interface Moveable {
     void move(Point destination);
     void stop();
     Point getPosition();

}
