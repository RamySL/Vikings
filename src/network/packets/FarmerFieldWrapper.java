package network.packets;

/**
 * FarmerFieldWrapper is a class that represents the coordinates of a farmer and a field.
 * It contains the x and y coordinates of both the farmer and the field, as well as a boolean indicating if the field is planted.
 * This class is used to wrap the information about the farmer and field coordinates together.
 */
public class FarmerFieldWrapper {
    private int farmerX;
    private int farmerY;
    private int fieldX;
    private int fieldY;
    private boolean isPlanted;

    /**
     * Constructor for the FarmerFieldWrapper class.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     * @param isPlanted A boolean indicating if the field is planted.
     */
    public FarmerFieldWrapper(int farmerX, int farmerY, int fieldX, int fieldY, boolean isPlanted) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
        this.isPlanted=isPlanted;
    }

    /**
     * Getters for the coordinates of the farmer and the field.
     * @return The x or y coordinate of the farmer or field.
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
     * @return The x coordinate of the field.
     */
    public int getFieldX() {
        return fieldX;
    }

    /**
     * @return The y coordinate of the field.
     */
    public int getFieldY() {
        return fieldY;
    }

    /**
     * @return A boolean indicating if the field is planted.
     */
    public boolean getIsPlanted() {
        return isPlanted;
    }
}