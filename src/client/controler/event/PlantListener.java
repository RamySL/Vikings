package client.controler.event;

import java.util.EventListener;

/**
 * PlantListener interface defines a listener for PlantEvent.
 * It contains a method onPlant that is called when a PlantEvent occurs.
 */
public interface PlantListener extends EventListener {
    void onPlant(PlantEvent event);
}