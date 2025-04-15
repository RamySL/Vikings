package network.packets;

import java.util.List;

public class PaquetEndGame {
    private List<Integer> winningCampIds;

    public PaquetEndGame(List<Integer> winningCampIds) {
        this.winningCampIds = winningCampIds;
    }

    public List<Integer> getWinningCampIds() {
        return winningCampIds;
    }
}