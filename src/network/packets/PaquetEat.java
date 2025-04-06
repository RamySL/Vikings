package network.packets;

/**
 * PaquetEat is a class that represents the coordinates of a farmer and an animal.
 * It contains the x and y coordinates of both the farmer and the animal.
 * This class is used to wrap the information about the farmer and animal coordinates together.
 */
public class PaquetEat {
    private int farmerX, farmerY, animalX, animalY;


    /**
     * Constructor for the PaquetEat class.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param animalX The x-coordinate of the animal.
     * @param animalY The y-coordinate of the animal.
     */
    public PaquetEat( int farmerX, int farmerY, int animalX, int animalY) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.animalX = animalX;
        this.animalY = animalY;
    }


    /**
     * Getters for the coordinates of the farmer and the animal.
     * @return The x or y coordinate of the farmer or animal.
     */
    /**
     * @return The x coordinate of the farmer.
     */
    public int getFarmerX() {
        return farmerX;
    }

    /**
     * @return The y coordinate of the farmer.
     */
    public int getFarmerY() {
        return farmerY;
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