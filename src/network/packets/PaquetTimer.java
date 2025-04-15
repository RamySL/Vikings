package network.packets;

public class PaquetTimer {
    private int remainingTime;

    public PaquetTimer(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}