package model;

/**
 * Unite class
 * This class represents a unit in the game.
 * It provides methods to get and set the position of the unit, set the destination of the unit, move the unit to the destination, and check if the unit has reached the destination.
 */
public class Unite {
    private String id;
    private int x, y; // Position de l'unité
    private int destinationX, destinationY;

    /**
     * Constructor for the Unite class.
     * Initializes the ID, x, and y coordinates of the unit to the specified values.
     *
     * @param id The ID of the unit.
     * @param x The x-coordinate of the unit.
     * @param y The y-coordinate of the unit.
     */
    public Unite(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.destinationX = x;
        this.destinationY = y;
    }

    /*getters*/
    /**
     * Returns the ID of the unit.
     *
     * @return The ID of the unit.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the x-coordinate of the unit.
     *
     * @return The x-coordinate of the unit.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the unit.
     *
     * @return The y-coordinate of the unit.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the x-coordinate of the destination of the unit.
     *
     * @return The x-coordinate of the destination of the unit.
     */
    public void setDestination(int x, int y) {
        this.destinationX = x;
        this.destinationY = y;
    }

    /**
     * Returns the y-coordinate of the destination of the unit.
     *
     * @return The y-coordinate of the destination of the unit.
     */
    public void deplacer() {

        // Déplacement de l'unité vers la destination en ligne droite
        if (x < destinationX) {
            x++;
        } else if (x > destinationX) {
            x--;
        }

        if (y < destinationY) {
            y++;
        } else if (y > destinationY) {
            y--;
        }

    }

    /**
     * Returns true if the unit has reached the destination, false otherwise.
     *
     * @return True if the unit has reached the destination, false otherwise.
     */
    public boolean aAtteintDestination() {
        return x == destinationX && y == destinationY;
    }
}