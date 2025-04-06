package client.controler.event;

public class ExitEvent {
    private String message;

    public ExitEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
