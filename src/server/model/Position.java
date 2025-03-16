package server.model;

import java.awt.*;
import java.util.HashMap;

/**
 * Classe that contains all that concerns model coordinates
 */
public class Position {
    // See the doc
    public static final int MARGIN = 10;
    // width of a camp
    public static final int WIDTH = 200;
    // height of a camp
    public static final int HEIGHT = 150;
    // map betwwen camp id and the coordinates of the top left corner of the camp
    public static final HashMap<Integer, Point> MAP_CAMPS_POSITION = new HashMap<Integer, Point>(){
        {
            put(0, new Point(MARGIN, MARGIN + HEIGHT));
            put(1, new Point(3 * MARGIN + WIDTH, MARGIN + HEIGHT));
            put(2, new Point(3 * MARGIN + WIDTH, 3 * MARGIN + 2 * HEIGHT));
            put(3, new Point(MARGIN, 3 * MARGIN + 2 * HEIGHT));
        }
    };

    /**
     * Check if the click is on the camp with the given id
     * @param campId
     * @param clickX
     * @param clickY
     * @return
     */
    public static boolean isInCamp(int campId, int clickX, int clickY){
        Point camp = MAP_CAMPS_POSITION.get(campId);
        return clickX >= camp.x && clickX <= camp.x + WIDTH && clickY <= camp.y && clickY >= camp.y - HEIGHT;
    }

}
