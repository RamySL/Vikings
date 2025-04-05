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
    private int farmerX, farmerY, fieldX, fieldY;

    /**
     * Constructor for the PaquetPlant class.
     * @param resource The resource being planted.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     */
    public PaquetPlant(String resource, int farmerX, int farmerY, int fieldX, int fieldY) {
        this.resource = resource;
        this.farmerX=farmerX;
        this.farmerY=farmerY;
        this.fieldX=fieldX;
        this.fieldY=fieldY;
    }

    /**
     * Getters for the coordinates of the farmer and the field.
     * @return
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
