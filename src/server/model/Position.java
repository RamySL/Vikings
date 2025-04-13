package server.model;

import client.view.ViewPartie;

import java.awt.*;
import java.util.HashMap;

/**
 * Classe that contains all that concerns model coordinates
 */
public class Position {
    // See the doc
    public static final int MARGIN = 50;
    // width of a camp
    public static final int WIDTH = 250;
    // height of a camp
    public static final int HEIGHT = 200;
    // from top left of the entities
    public static final int WIDTH_VIKINGS = 10, HEIGHT_VIKINGS = 10;
    public static final int WIDTH_WHEAT = 10, HEIGHT_WHEAT = 10;
    public static final int WIDTH_SHEEP = 10, HEIGHT_SHEEP = 10;
    public static final int WIDTH_FIELD = 30, HEIGHT_FIELD = 30;
    public static final int WIDTH_COW = 10, HEIGHT_COW = 10;
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
     * a méthode that tranforms view coordinates into model coordinates using :
     * -  removing translation
     * - removing scale
     *  - x = x’/RatioX et y = height + 2 × margin − (y′/RatioY )
     *  ? est-ce que mauvaise pratique d'avoir utiliser ViewPartie.RATIO_X
     */
    public static Point viewToModel(Point viewPoint, Point translation, double scale) {
        viewPoint.translate(-translation.x,-translation.y);
        viewPoint.x = (int) (viewPoint.x / scale);
        viewPoint.y = (int) (viewPoint.y / scale);
        return new Point(viewPoint.x / ViewPartie.RATIO_X, HEIGHT+2*MARGIN - (viewPoint.y / ViewPartie.RATIO_Y));
    }

    /**
     * Check if the click is on the camp with the given id<p>
     * !! takes model coord
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
