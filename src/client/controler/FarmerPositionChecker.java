package client.controler;

import server.model.*;

import java.awt.Point;


public class FarmerPositionChecker extends VikingPositionChecker {
    private Farmer farmer;

    public FarmerPositionChecker(ControlerParty controlerParty, Camp camp, Camp nextCamp, Farmer farmer) {
        super(controlerParty, camp, nextCamp, farmer);
        this.farmer=farmer;
    }

    /**
     * The run method of the thread that continuously checks the farmer's position.
     * It sleeps for a specified interval before checking again.
     */
    @Override
    public void run() {
        while (true) {
            if(locked) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            checkFarmerNearField(this.farmer);
            super.checkNearLivestock(this.farmer);
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
    private void checkFarmerNearField(Farmer farmer) {
        Field nearestField = getNearestField((Farmer) viking);
        boolean nearField = isNearFieldWithMargin((Farmer) viking, Position.DISTANCE_TOLERANCE_FIELD, nearestField);
        this.controlerParty.setFarmerNearField(nearField, viking.getId(), nearestField.getId(), nearestField.isPlanted());
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


