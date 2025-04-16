package network.packets;

import java.util.List;

public class PaquetEndGame {
    private List<Integer> orderedCampIds;
    private List<String> playerNames; // Ajout de l'attribut pour les pseudos

    public PaquetEndGame(List<Integer> orderedCampIds, List<String> playerNames) {
        this.orderedCampIds = orderedCampIds;
        this.playerNames = playerNames; // Initialisation des pseudos
    }

    public List<Integer> getOrderedCampIds() {
        return orderedCampIds;
    }

    public List<String> getPlayerNames() {
        return playerNames; // Getter pour les pseudos
    }
}