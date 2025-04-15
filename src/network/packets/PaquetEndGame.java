package network.packets;

public class PaquetEndGame {
    private int winningCampId;


    public PaquetEndGame(int winningCampId) {
        this.winningCampId = winningCampId;
    }

    public int getWinningCampId() {
        return winningCampId;
    }

}