package network.packets;

public class PacketOpenPanelControl {
    private String message;

    public PacketOpenPanelControl() {
        this.message = "OpenPanelControl";
    }

    public String getMessage() {
        return message;
    }
}
