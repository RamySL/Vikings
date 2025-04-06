package network.packets;

/**
 * PaquetExit is a class that represents a packet used to send an exit event.
 * This class is used to notify the server that the exit event has occurred.
 */
public class PaquetExit {
    private String message;

    public PaquetExit(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}