package client.controler.event;


/** * HarvestEvent class represents an event where a Farmer harvests field.
 * It contains the coordinates of the Farmer and the field involved in the event.
 */
public class HarvestEvent {
    private int idFarmer;
    private int idField;

    /**
     * Constructor for the PaquetHarvest class.
     * @param idFarmer The ID of the viking.
     * @param idField The ID of the field.
     */
    public HarvestEvent(int idFarmer, int idField) {
        this.idFarmer = idFarmer;
        this.idField = idField;
    }

    /**
     * Getter for the ID of the viking.
     * @return The ID of the viking.
     */
    public int getIdFarmer() {
        return idFarmer;
    }

    /**
     * Getter for the ID of the field.
     * @return The ID of the field.
     */
    public int getIdField() {
        return idField;
    }
}