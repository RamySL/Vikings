package client.controler.event;


/** * HarvestEvent class represents an event where a Farmer harvests field.
 * It contains the coordinates of the Farmer and the field involved in the event.
 */
public class HarvestEvent {
    private int farmerX, farmerY, fieldX, fieldY;

    /** * Constructor for the EatEvent class.
     * @param farmerX The x-coordinate of the Farmer.
     * @param farmerY The y-coordinate of the Farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     */
    public HarvestEvent( int farmerX, int farmerY, int fieldX, int fieldY) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    /** * Getters for the coordinates of the Farmer and the field.
     * @return The x or y coordinate of the Farmer or field.
     */
    /**
     * @return The x coordinate of the Farmer.
     */
    public int getFarmerX() {
        return farmerX;
    }
    /**
     * @return The y coordinate of the Farmer.
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