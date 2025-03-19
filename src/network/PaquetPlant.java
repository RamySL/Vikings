package network;

import com.google.gson.Gson;

public class PaquetPlant {
    private String resource;
    private int fieldId;
    private final ThreadCommunicationServer communicationServer;

    // Updated constructor to accept communicationServer
    public PaquetPlant(String resource, int fieldId, ThreadCommunicationServer communicationServer) {
        this.resource = resource;
        this.fieldId = fieldId;
        this.communicationServer = communicationServer; // Initialize the communicationServer
    }

    public String getResource() {
        return resource;
    }

    public int getFieldId() {
        return fieldId;
    }

    /*public void sendPlantPacketToServer() {
        PacketWrapper packetWrapper = new PacketWrapper();
        packetWrapper.type = "PaquetPlant";
        packetWrapper.content = new Gson().toJsonTree(this);

        String jsonPacket = new Gson().toJson(packetWrapper);
        communicationServer.sendMessage(jsonPacket); // Use the communicationServer to send the message
    }*/
}
