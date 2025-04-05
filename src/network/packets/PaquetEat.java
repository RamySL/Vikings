package network.packets;

/**
 * PaquetEat is a class that represents the coordinates of a viking and an animal.
 * It contains the x and y coordinates of both the viking and the animal.
 * This class is used to wrap the information about the viking and animal coordinates together.
 */
public class PaquetEat {
    private int vikingX, vikingY, animalX, animalY;

    /**
     * Constructor for the PaquetEat class.
     * @param vikingX The x-coordinate of the viking.
     * @param vikingY The y-coordinate of the viking.
     * @param animalX The x-coordinate of the animal.
     * @param animalY The y-coordinate of the animal.
     */
    public PaquetEat( int vikingX, int vikingY, int animalX, int animalY) {
        this.vikingX = vikingX;
        this.vikingY = vikingY;
        this.animalX = animalX;
        this.animalY = animalY;
    }


    /**
     * Getters for the coordinates of the viking and the animal.
     * @return The x or y coordinate of the viking or animal.
     */
    /**
     * @return The x coordinate of the viking.
     */
    public int getvikingX() {
        return vikingX;
    }

    /**
     * @return The y coordinate of the viking.
     */
    public int getVikingY() {
        return vikingY;
    }

    /**
     * @return The x coordinate of the animal.
     */
    public int getAnimalXX() {
        return animalX;
    }

    /**
     * @return The y coordinate of the animal.
     */
    public int getAnimalYY() {
        return animalY;
    }
}