package client.controler.event;

import java.util.EventListener;

/**
 * ExitListener interface defines a listener for ExitEvent.
 * It contains a method onExit that is called when an ExitEvent occurs.
 */
public interface ExitListenner extends EventListener {
    /**
     * Called when an ExitEvent occurs.
     * @param event The ExitEvent that occurred.
     */
    void onExit(ExitEvent event);
}