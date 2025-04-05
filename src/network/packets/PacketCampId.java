package network.packets;

/**
 * the packet that will announce to the client his camp id
 * this packet is sent by the server to the client
 */
public class PacketCampId {
    private int campId;

    /**
     * Constructor for the PacketCampId class.
     * @param campId The camp ID to be set.
     */
    public PacketCampId(int campId) {
        this.campId = campId;
    }

    /**
     * Getters for the camp ID.
     * @return The camp ID.
     */
    public int getCampId() {
        return campId;
    }
}
