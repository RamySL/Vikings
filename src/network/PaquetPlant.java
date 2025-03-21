package network;

import com.google.gson.Gson;
import server.model.*;

public class PaquetPlant {
    private String resource;
    private int farmerX, farmerY, fieldX, fieldY;

    // Updated constructor to accept communicationServer
    public PaquetPlant(String resource, int farmerX, int farmerY, int fieldX, int fieldY) {
        this.resource = resource;
        this.farmerX=farmerX;
        this.farmerY=farmerY;
        this.fieldX=fieldX;
        this.fieldY=fieldY;
    }

    public String getResource() {
        return resource;
    }
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



    public void sendPlantPacketToServer(Client client) {
        System.out.println("heyyyyyyyyyy");
        PacketWrapper packetWrapper = new PacketWrapper();
        packetWrapper.type = "PaquetPlant";
        packetWrapper.content = new Gson().toJsonTree(this);

        String jsonPacket = new Gson().toJson(packetWrapper);

    }
}
