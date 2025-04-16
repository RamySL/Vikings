package network.packets;

public  class PacketFailed {
    private String reason;

    public PacketFailed(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }


}
