package network.packets;

/**
 * PaquetHarvest is a class that represents a packet used to send harvesting information in the game.
 * It contains information about the coordinates of the farmer and the field.
 * This class is used to send harvesting information from the client to the server.
 */
public class PaquetHarvest {
    private int farmerX, farmerY, fieldX, fieldY;

    /**
     * Constructor for the PaquetHarvest class.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     */
    public PaquetHarvest(int farmerX, int farmerY, int fieldX, int fieldY) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    /**
     * Getters for the coordinates of the farmer and the field.
     */
    public int getFarmerX() {
        return farmerX;
    }

    public int getFarmerY() {
        return farmerY;
    }

    public int getFieldX() {
        return fieldX;
    }

    public int getFieldY() {
        return fieldY;
    }
}