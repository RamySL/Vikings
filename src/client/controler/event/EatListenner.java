package client.controler.event;

/**
 * EatListenner interface defines a listener for EatEvent.
 * It contains a method onEat that is called when an EatEvent occurs.
 */
public interface EatListenner {
    void onEat(EatEvent event);
}
