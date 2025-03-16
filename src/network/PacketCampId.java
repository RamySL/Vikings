package network;

/**
 * the packet that will announe to the client his camp id
 */
public class PacketCampId {
    private int campId;

    public PacketCampId(int campId) {
        this.campId = campId;
    }

    public int getCampId() {
        return campId;
    }
}
