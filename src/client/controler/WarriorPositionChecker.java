package client.controler;

import server.model.Camp;
import server.model.Position;
import server.model.Sheep;
import server.model.Viking;

public class WarriorPositionChecker extends VikingPositionChecker{

    public WarriorPositionChecker(ControlerParty controlerParty, Camp camp, Camp nextCamp, Viking viking) {
        super(controlerParty, camp, nextCamp, viking);
    }
    /**
     * The run method of the thread that continuously checks the farmer's position.
     * It sleeps for a specified interval before checking again.
     */
    @Override
    public void run() {
        while (true) {
            if(locked) {
                System.out.println("locked " + viking.getId());
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

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
