package network.server;

import network.packets.FormatPacket;
import server.model.*;

import java.awt.Point;

/**
 * FarmerPositionChecker is a thread that checks the position of a farmer in relation to the fields in a camp.
 * It sends messages to the communication server when the farmer is near or not near a field.
 * This class is used to monitor the farmer's position and notify the client about the status of the farmer's proximity to the fields.
 * It runs in a separate thread and checks the position at regular intervals.
 * The distance tolerance can be set to determine how close the farmer needs to be to a field to be considered "near".
 * The class also keeps track of the previous state of the farmer's proximity to avoid sending duplicate messages.
 */
public class FarmerPositionChecker extends Thread {
    private static final int CHECK_INTERVAL_MS = 500;
    private final ThreadCommunicationServer communicationServer;
    private final Camp camp;
    private final Farmer farmer;
    private final double distanceTolerance;
    private boolean previousNearFieldState;
    private boolean previousNearSheepState;

    /**
     * Constructor for the FarmerPositionChecker class.
     * @param communicationServer The communication server used to send messages to the client.
     * @param camp The camp where the farmer is located.
     * @param farmer The farmer whose position is being checked.
     * @param distanceTolerance The distance tolerance for determining if the farmer is near a field.
     */
    public FarmerPositionChecker(ThreadCommunicationServer communicationServer, Camp camp, Farmer farmer, double distanceTolerance) {
        this.communicationServer = communicationServer;
        this.camp = camp;
        this.farmer = farmer;
        this.distanceTolerance = distanceTolerance;
        this.previousNearFieldState = false;
        this.previousNearSheepState = false;
    }

    /**
     * The run method of the thread that continuously checks the farmer's position.
     * It sleeps for a specified interval before checking again.
     */
    @Override
    public void run() {
        while (true) {
            checkFarmerNearField();
            checkFarmerNearSheep();
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Checks if the farmer is near a field and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the farmer is near a field.
     */
   private void checkFarmerNearField() {
       Field nearestField = getNearestField(farmer);
       boolean nearField = isNearFieldWithMargin(farmer, distanceTolerance, nearestField);
       if (nearField != previousNearFieldState) {
           String message = String.format("{\"farmerX\": %d, \"farmerY\": %d, \"fieldX\": %d, \"fieldY\": %d, \"isPlanted\": %b}",
                   farmer.getPosition().x, farmer.getPosition().y,
                   nearestField.getPosition().x, nearestField.getPosition().y, nearestField.isPlanted());
           if (nearField) {
               communicationServer.sendMessage(FormatPacket.format("FarmerNearField", message ));
           } else {
               communicationServer.sendMessage(FormatPacket.format("FarmerNotNearField", message ));
           }
           previousNearFieldState = nearField;
       }
   }

    /**
     * Checks if the farmer is near a field with a specified margin.
     * @param farmer The farmer whose position is being checked.
     * @param margin The distance margin for determining if the farmer is near a field.
     * @param field The field to check against.
     * @return True if the farmer is near the field, false otherwise.
     */
    private boolean isNearFieldWithMargin(Farmer farmer, double margin, Field field) {
        double distanceToField = farmer.getPosition().distance(field.getPosition());
        return distanceToField <= margin;
    }

    /**
     * Finds the nearest field to the farmer.
     * @param farmer The farmer whose position is being checked.
     * @return The nearest field to the farmer.
     */
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

    /**
     * Checks if the farmer is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the farmer is near a sheep.
     * @param farmer The farmer whose position is being checked.
     * @return the nearest sheep to the farmer.
     */
    private Sheep getNearestSheep(Farmer farmer) {
        Point farmerPosition = farmer.getPosition();
        double minDistance = Double.MAX_VALUE;
        Sheep nearestSheep = null;
        for (Sheep sheep : camp.getSheep()) {
            double distance = farmerPosition.distance(sheep.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                nearestSheep = sheep;
            }
        }
        return nearestSheep;
    }

    /**
     * Checks if the farmer is near a sheep with a specified margin.
     * @param farmer The farmer whose position is being checked.
     * @param margin The distance margin for determining if the farmer is near a sheep.
     * @param sheep The sheep to check against.
     * @return True if the farmer is near the sheep, false otherwise.
     */
    private boolean isNearSheepWithMargin(Farmer farmer, double margin, Sheep sheep) {
        double distanceToSheep = farmer.getPosition().distance(sheep.getPosition());
        return distanceToSheep <= margin;
    }

    /**
     * Checks if the farmer is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the farmer is near a sheep.
     */
    private void checkFarmerNearSheep() {
        Sheep nearestSheep = getNearestSheep(farmer);
        boolean nearSheep = isNearSheepWithMargin(farmer, distanceTolerance, nearestSheep);
        if (nearSheep != previousNearSheepState) {
            String message = String.format("{\"farmerX\": %d, \"farmerY\": %d, \"sheepX\": %d, \"sheepY\": %d}",
                    farmer.getPosition().x, farmer.getPosition().y,
                    nearestSheep.getPosition().x, nearestSheep.getPosition().y);
            if (nearSheep) {
                communicationServer.sendMessage(FormatPacket.format("FarmerNearSheep", message ));
            } else {
                communicationServer.sendMessage(FormatPacket.format("FarmerNotNearSheep", message ));
            }
            previousNearSheepState = nearSheep;
        }
    }

}