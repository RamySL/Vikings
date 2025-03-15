package network;

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
        boolean nearField = isNearFieldWithMargin(farmer, distanceTolerance);
        if (nearField != previousNearFieldState) {
            if (nearField) {
                System.out.println("Farmer is near a field with margin, sending message to client.");
                communicationServer.sendMessage(FormatPacket.format("FarmerNearField", "{}"));
            } else {
                System.out.println("Farmer is not near a field, sending message to client.");
                communicationServer.sendMessage(FormatPacket.format("FarmerNotNearField", "{}"));
            }
            previousNearFieldState = nearField;
        }
    }

    private boolean isNearFieldWithMargin(Farmer farmer, double margin) {
        double distanceToField = calculateDistanceToField(farmer);
        return distanceToField <= margin;
    }

    private double calculateDistanceToField(Farmer farmer) {
        Point farmerPosition = farmer.getPosition();
        double minDistance = Double.MAX_VALUE;
        for (Field field : camp.getFields()) {
            Point fieldPosition = field.getPosition();
            double distance = farmerPosition.distance(fieldPosition);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
}