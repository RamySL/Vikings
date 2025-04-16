package server.model;

import client.view.ViewPartie;

import java.awt.*;
import java.util.HashMap;

/**
 * Classe that contains all that concerns model coordinates
 */
public class Position {
    // See the doc (margin to ceneter the camp)
    public static final int MARGIN_X = 50;
    public static final int MARGIN_Y = 20;
    public static final int SPACING_CAMP = 150;
    // width of a camp
    public static final int WIDTH = 350;
    // height of a camp
    public static final int HEIGHT = 250;
    // from top left of the entities
    public static final int WIDTH_VIKINGS = 20, HEIGHT_VIKINGS = 20;
    public static final int WIDTH_WHEAT = 20, HEIGHT_WHEAT =20;
    public static final int WIDTH_SHEEP = 20, HEIGHT_SHEEP = 20;
    public static final int WIDTH_FIELD = 50, HEIGHT_FIELD = 35;
    public static final int WIDTH_COW = 20, HEIGHT_COW = 20;
    public static final int WIDTH_ENCLOS = 60, HEIGHT_ENCLOS = 40;
    // distance tolerance for the farmer to be considered on the field
    public static final int DISTANCE_TOLERANCE_FIELD = WIDTH_FIELD/2;
    // distance tolerance for the viking to be considered near a sheep
    public static final int DISTANCE_TOLERANCE_SHEEP = WIDTH_SHEEP/2 + WIDTH_VIKINGS/2 + 4;

    // map betwwen camp id and the coordinates of the top left corner of the camp
    public static final HashMap<Integer, Point> MAP_CAMPS_POSITION = new HashMap<Integer, Point>(){
        {
            put(0, new Point(MARGIN_X, MARGIN_Y + HEIGHT));
            put(1, new Point(3 * MARGIN_X + WIDTH + SPACING_CAMP, MARGIN_Y + HEIGHT));
            put(2, new Point(3 * MARGIN_X + WIDTH + SPACING_CAMP, 3 * MARGIN_Y + 2 * HEIGHT + SPACING_CAMP));
            put(3, new Point(MARGIN_X, 3 * MARGIN_Y + 2 * HEIGHT + SPACING_CAMP));
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
        return new Point(viewPoint.x / ViewPartie.RATIO_X, HEIGHT+2*MARGIN_Y - (viewPoint.y / ViewPartie.RATIO_Y));
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
