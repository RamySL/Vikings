package network.packets;

/**
 * PaquetHarvest is a class that represents a packet used to send harvesting information in the game.
 * It contains information about the coordinates of the farmer and the field.
 * This class is used to send harvesting information from the client to the server.
 */
public class PaquetHarvest {
    private int idFarmer;
    private int idField;

    /**
     * Constructor for the PaquetHarvest class.
     * @param idFarmer The ID of the viking.
     * @param idField The ID of the field.
     */
    public PaquetHarvest(int idFarmer, int idField) {
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