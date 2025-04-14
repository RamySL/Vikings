package client.controler.event;

import java.util.EventListener;

/**
 * HarvestListener interface defines a listener for HarvestEvent.
 * It contains a method onHarvest that is called when a HarvestEvent occurs.
 */
public interface HarvestListenner extends EventListener {
    void onHarvest(HarvestEvent event);
}