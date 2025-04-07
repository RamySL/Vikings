package client.controler.event;


/** * EatEvent class represents an event where a Viking eats an animal.
 * It contains the coordinates of the Viking and the animal involved in the event.
 */
public class EatEvent {
    private int vikingX, vikingY, animalX, animalY;

    /** * Constructor for the EatEvent class.
     * @param vikingX The x-coordinate of the Viking.
     * @param vikingY The y-coordinate of the Viking.
     * @param animalX The x-coordinate of the animal.
     * @param animalY The y-coordinate of the animal.
     */
    public EatEvent( int vikingX, int vikingY, int animalX, int animalY) {
        this.vikingX = vikingX;
        this.vikingY = vikingY;
        this.animalX = animalX;
        this.animalY = animalY;
    }

    /** * Getters for the coordinates of the Viking and the animal.
     * @return The x or y coordinate of the Viking or animal.
     */
    /**
     * @return The x coordinate of the Viking.
     */
    public int getVikingX() {
        return vikingX;
    }
    /**
     * @return The y coordinate of the Viking.
     */
    public int getVikingY() {
        return vikingY;
    }

    /**
     * @return The x coordinate of the animal.
     */
    public int getAnimalX() {
        return animalX;
    }

    /**
     * @return The y coordinate of the animal.
     */
    public int getAnimalY() {
        return animalY;
    }
}