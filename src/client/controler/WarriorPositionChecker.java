package client.controler;

import server.model.Camp;
import server.model.Viking;

public class WarriorPositionChecker extends VikingPositionChecker{
    private Viking viking;

    public WarriorPositionChecker(ControlerParty controlerParty, Camp camp, Camp nextCamp, Viking viking) {
        super(controlerParty, camp, nextCamp, viking);
        this.viking = viking;

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
            super.checkNearLivestock(this.viking);
            this.update();
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
