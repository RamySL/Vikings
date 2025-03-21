package client.controler.event;

import java.util.EventListener;

public interface PlantListener extends EventListener {
    void onPlant(PlantEvent event);
}