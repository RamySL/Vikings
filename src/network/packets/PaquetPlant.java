package network.packets;

import com.google.gson.Gson;
import network.client.Client;

/**
 * PaquetPlant is a class that represents a packet used to send planting information in the game.
 * It contains information about the resource being planted, the coordinates of the farmer, and the coordinates of the field.
 * This class is used to send planting information from the client to the server.
 */
public class PaquetPlant {
    private String resource;
    private int idFarmer;
    private int idField;

    /**
     * Constructor for the PaquetPlant class.
     * @param resource The resource being planted.
     * @param idFarmer The ID of the viking.
     * @param idField The ID of the field.
     */
    public PaquetPlant(String resource, int idFarmer, int idField) {
        this.resource = resource;
        this.idFarmer = idFarmer;
        this.idField = idField;
    }

    /**
     * Getter for the resource being planted.
     * @return The resource being planted.
     */
    public String getResource() {
        return resource;
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
