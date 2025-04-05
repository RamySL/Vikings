package client.controler.event;

/**
 * PlantEvent class represents an event where a farmer plants a resource in a field.
 * It contains the coordinates of the farmer and the field involved in the event.
 */
public class PlantEvent {
    private String resource;
    private int farmerX, farmerY, fieldX, fieldY;

    /**
     * Constructor for the PlantEvent class.
     * @param resource The resource being planted.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     */
    public PlantEvent( String resource, int farmerX, int farmerY, int fieldX, int fieldY) {
        this.resource = resource;
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    /**
     * Getters for the coordinates of the farmer and the field.
     * @return The x or y coordinate of the farmer or field.
     */
    /**
     * @return The resource being planted.
     */
    public String getResource() {
        return resource;
    }

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
}