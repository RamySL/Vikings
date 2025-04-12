package client.controler;

import server.model.Camp;
import server.model.Sheep;
import server.model.Viking;

import java.awt.*;

/**
 * Tracks the position of vikings enabling action in the camp depending on the pisition
 */
public abstract class VikingPositionChecker extends Thread{
    protected ControlerParty controlerParty;
    protected static final int CHECK_INTERVAL_MS = 500;
    protected Camp camp;
    protected final double distanceTolerance;
    protected boolean previousNearFieldState;
    protected boolean previousNearSheepState;

    protected Viking viking;
    protected Camp nextCamp;


    /**
     * Constructor for the VikingPositionChecker class.
     * @param controlerParty The controler party instance.
     * @param camp The camp instance.
     * @param viking The viking instance.
     * @param distanceTolerance The distance tolerance for checking proximity.
     */
    public VikingPositionChecker(ControlerParty controlerParty,  Camp camp, Camp nextCamp, Viking viking, double distanceTolerance) {
        this.controlerParty = controlerParty;
        this.camp = camp;
        this.nextCamp = nextCamp;
        this.viking = viking;
        this.distanceTolerance = distanceTolerance;
        this.previousNearFieldState = false;
        this.previousNearSheepState = false;
    }

    /**
     * Checks if the viking is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the viking is near a sheep.
     * @param viking The viking whose position is being checked.
     * @return the nearest sheep to the viking.
     */
    private Sheep getNearestSheep(Viking viking) {
        Point farmerPosition = viking.getPosition();
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
     * Checks if the viking is near a sheep with a specified margin.
     * @param viking The viking whose position is being checked.
     * @param margin The distance margin for determining if the viking is near a sheep.
     * @param sheep The sheep to check against.
     * @return True if the viking is near the sheep, false otherwise.
     */
    protected boolean isNearSheepWithMargin(Viking viking, double margin, Sheep sheep) {
        double distanceToSheep = viking.getPosition().distance(sheep.getPosition());
        return distanceToSheep <= margin;
    }

    /**
     * Checks if the farmer is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the farmer is near a sheep.
     */
    protected void checkNearSheep() {
        Sheep nearestSheep = getNearestSheep(viking);
        boolean nearSheep = isNearSheepWithMargin(viking, distanceTolerance, nearestSheep);
        if (nearSheep != previousNearSheepState) {
            this.controlerParty.setFarmerNearSheep(nearSheep, viking.getId(), nearestSheep.getId());
            previousNearSheepState = nearSheep;
        }
    }

    public void update(){
        this.camp = nextCamp;
        for(Viking viking : nextCamp.getVikings()){
            if(viking.getId() == this.viking.getId()){
                this.viking = viking;
            }
        }
    }

    public void setNextCamp(Camp camp){
        this.nextCamp = camp;
    }





}
