package client.controler.event;

/**
 * PlantEvent class represents an event where a farmer plants a resource in a field.
 * It contains the coordinates of the farmer and the field involved in the event.
 */
public class PlantEvent {
    private String resource;
    private int idFarmer, IdField;

    /**
     * Constructor to create a PlantEvent with the specified resource, farmer ID, and field ID.
     *
     * @param resource  The type of resource being planted.
     * @param idFarmer  The ID of the farmer involved in the event.
     * @param idField   The ID of the field where the resource is being planted.
     */
    public PlantEvent(String resource, int idFarmer, int idField) {
        this.resource = resource;
        this.idFarmer = idFarmer;
        IdField = idField;
    }
    /**
     * Gets the type of resource being planted.
     *
     * @return The type of resource.
     */
    public String getResource() {
        return resource;
    }
    /**
     * Gets the ID of the farmer involved in the event.
     *
     * @return The ID of the farmer.
     */
    public int getIdFarmer() {
        return idFarmer;
    }

    /**
     * Gets the ID of the field where the resource is being planted.
     *
     * @return The ID of the field.
     */
    public int getIdField() {
        return IdField;
    }

}