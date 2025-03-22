package network.server;

import network.packets.FormatPacket;
import server.model.Camp;
import server.model.Farmer;
import server.model.Field;

import java.awt.Point;

public class FarmerPositionChecker extends Thread {
    private static final int CHECK_INTERVAL_MS = 500;
    private final ThreadCommunicationServer communicationServer;
    private final Camp camp;
    private final Farmer farmer;
    private final double distanceTolerance;
    private boolean previousNearFieldState;

    public FarmerPositionChecker(ThreadCommunicationServer communicationServer, Camp camp, Farmer farmer, double distanceTolerance) {
        this.communicationServer = communicationServer;
        this.camp = camp;
        this.farmer = farmer;
        this.distanceTolerance = distanceTolerance;
        this.previousNearFieldState = false;
    }

    @Override
    public void run() {
        while (true) {
            checkFarmerNearField();
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



   private void checkFarmerNearField() {
       Field nearestField = getNearestField(farmer);
       boolean nearField = isNearFieldWithMargin(farmer, distanceTolerance, nearestField);
       if (nearField != previousNearFieldState) {
           String message = String.format("{\"farmerX\": %d, \"farmerY\": %d, \"fieldX\": %d, \"fieldY\": %d, \"isPlanted\": %b}",
                   farmer.getPosition().x, farmer.getPosition().y,
                   nearestField.getPosition().x, nearestField.getPosition().y, nearestField.isPlanted());
           if (nearField) {
               //System.out.println("Farmer is near a field with margin, sending message to client.");
               communicationServer.sendMessage(FormatPacket.format("FarmerNearField", message ));
           } else {
               //System.out.println("Farmer is not near a field, sending message to client.");
               communicationServer.sendMessage(FormatPacket.format("FarmerNotNearField", message ));
           }
           previousNearFieldState = nearField;
       }
   }

    private boolean isNearFieldWithMargin(Farmer farmer, double margin, Field field) {
        double distanceToField = farmer.getPosition().distance(field.getPosition());
        return distanceToField <= margin;
    }

    private Field getNearestField(Farmer farmer) {
        Point farmerPosition = farmer.getPosition();
        double minDistance = Double.MAX_VALUE;
        Field nearestField = null;
        for (Field field : camp.getFields()) {
            double distance = farmerPosition.distance(field.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                nearestField = field;
            }
        }
        return nearestField;
    }

}