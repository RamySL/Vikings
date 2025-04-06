package network.packets;

/**
 * the packet that will announe to the client his camp id and the number of players in the party
 */
public class PacketCampIdNbPlayers {
    private int campId;
    private int nbPlayers;

    public PacketCampIdNbPlayers(int campId, int nbPlayers) {
        this.campId = campId;
        this.nbPlayers = nbPlayers;
    }

    public int getCampId() {
        return campId;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }
}

