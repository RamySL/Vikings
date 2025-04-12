package client.controler;

import network.packets.FarmerFieldWrapper;
import network.packets.FarmerSheepWrapper;
import network.packets.FormatPacket;
import network.server.ThreadCommunicationServer;
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
public class FarmerPositionChecker extends VikingPositionChecker {

    public FarmerPositionChecker(ControlerParty controlerParty, Camp camp, Camp nextCamp, Farmer farmer, double distanceTolerance) {
        super(controlerParty, camp, nextCamp, farmer, distanceTolerance);
    }

    /**
     * The run method of the thread that continuously checks the farmer's position.
     * It sleeps for a specified interval before checking again.
     */
    @Override
    public void run() {
        while (true) {
            checkFarmerNearField();
            super.checkNearSheep();
            this.update();
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
        Field nearestField = getNearestField((Farmer) viking);
        boolean nearField = isNearFieldWithMargin((Farmer) viking, distanceTolerance, nearestField);
        if (nearField != previousNearFieldState) {

            this.controlerParty.setFarmerNearField(nearField, viking.getId(), nearestField.getId(), nearestField.isPlanted());
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

}


