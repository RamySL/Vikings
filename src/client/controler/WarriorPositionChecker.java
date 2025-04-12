package client.controler;

import server.model.Camp;
import server.model.Viking;

public class WarriorPositionChecker extends VikingPositionChecker{

    public WarriorPositionChecker(ControlerParty controlerParty, Camp camp, Camp nextCamp, Viking viking, double distanceTolerance) {
        super(controlerParty, camp, nextCamp, viking, distanceTolerance);
    }
    /**
     * The run method of the thread that continuously checks the farmer's position.
     * It sleeps for a specified interval before checking again.
     */
    @Override
    public void run() {
        while (true) {
            super.checkNearSheep();
            this.update();
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
