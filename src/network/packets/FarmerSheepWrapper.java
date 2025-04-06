package network.packets;

/**
 * FarmerSheepWrapper is a class that represents the coordinates of a farmer and a sheep.
 * It contains the x and y coordinates of both the farmer and the sheep.
 * This class is used to wrap the information about the farmer and sheep coordinates together.
 */
public class FarmerSheepWrapper {
    private int farmerX;
    private int farmerY;
    private int sheepX;
    private int sheepY;

    /**
     * Constructor for the FarmerSheepWrapper class.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param sheepX The x-coordinate of the sheep.
     * @param sheepY The y-coordinate of the sheep.
     */
    public FarmerSheepWrapper(int farmerX, int farmerY, int sheepX, int sheepY) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.sheepX = sheepX;
        this.sheepY = sheepY;
    }

    /**
     * Getters for the coordinates of the farmer and the sheep.
     * @return The x or y coordinate of the farmer or sheep.
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
     * @return The x coordinate of the sheep.
     */
    public int getSheepX() {
        return sheepX;
    }

    /**
     * @return The y coordinate of the sheep.
     */
    public int getSheepY() {
        return sheepY;
    }
}