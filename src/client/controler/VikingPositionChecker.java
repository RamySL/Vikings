package client.controler;

import server.model.*;

import java.awt.*;

/**
 * Tracks the position of vikings enabling action in the camp depending on the pisition
 */
public abstract class VikingPositionChecker extends Thread{
    protected ControlerParty controlerParty;
    protected static final int CHECK_INTERVAL_MS = 50;
    protected Camp camp;

    protected Viking viking;
    protected Camp nextCamp;
    public boolean locked = true;
    public final Object lock = new Object();


    /**
     * Constructor for the VikingPositionChecker class.
     * @param controlerParty The controler party instance.
     * @param camp The camp instance.
     * @param viking The viking instance.
     */
    public VikingPositionChecker(ControlerParty controlerParty,  Camp camp, Camp nextCamp, Viking viking) {
        this.controlerParty = controlerParty;
        this.camp = camp;
        this.nextCamp = nextCamp;
        this.viking = viking;
    }

    /**
     * Checks if the viking is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the viking is near a sheep.
     * @param viking The viking whose position is being checked.
     * @return the nearest sheep to the viking.
     */
    protected Livestock getNearestLivestock() {
        Point farmerPosition = this.viking.getPosition();
        double minDistance = Double.MAX_VALUE;
        Livestock nearestSheep = null;
        for (Livestock livestock : camp.getLivestocks()) {
            double distance = farmerPosition.distance(livestock.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                nearestSheep = livestock;
            }
        }
        return nearestSheep;
    }

    /**
     * Checks if the viking is near a sheep with a specified margin.
     *
     * @param viking The viking whose position is being checked.
     * @param sheep  The sheep to check against.
     * @return True if the viking is near the sheep, false otherwise.
     */
    private boolean isNearLivestockWithMargin(Viking viking, Livestock sheep) {
        if (sheep == null) {
            return false;
        }
        double distanceToSheep = viking.getPosition().distance(sheep.getPosition());
        return distanceToSheep <= (double) Position.DISTANCE_TOLERANCE_SHEEP;
    }

    /**
     * Checks if the farmer is near a sheep and sends a message to the communication server if the state has changed.
     * It uses the distance tolerance to determine if the farmer is near a sheep.
     */
    protected void checkNearLivestock(Viking viking) {
        Livestock nearestSheep = getNearestLivestock();
        boolean nearSheep = isNearLivestockWithMargin(viking, nearestSheep);
        if(nearestSheep != null) {
            this.controlerParty.setVikingNearSheep(nearSheep, viking.getId(), nearestSheep.getId());
        }else{
            this.controlerParty.setVikingNearSheep(false, viking.getId(), -1);
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
    /**
     * pour actualiser avec ce que le serveur envoi
     * @param camp The next camp instance.
     */
    public void setNextCamp(Camp camp){
        this.nextCamp = camp;
    }





}
