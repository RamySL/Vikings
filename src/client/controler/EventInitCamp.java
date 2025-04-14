package client.controler;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Déclenché par la vu quand le camp est init
 */
public class EventInitCamp extends MouseEvent {

    public EventInitCamp(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button) {
        super(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
    }

    public EventInitCamp(Component source, int id) {
        this(source, id, System.currentTimeMillis(),0,0,0,0,false,0);
    }


}
