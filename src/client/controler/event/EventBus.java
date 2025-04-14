package client.controler.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * EventBus class is a singleton that manages event subscriptions and publications.
 * It allows different parts of the application to communicate with each other
 * by subscribing to and publishing events.
 */
public class EventBus {
    private static EventBus instance;
    private Map<String, Consumer<Object>> eventListeners = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private EventBus() {}

    /**
     * Returns the singleton instance of EventBus.
     * @return The singleton instance of EventBus.
     */
    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Subscribes a listener to an event type.
     * @param eventType The type of event to subscribe to.
     * @param listener The listener to be notified when the event occurs.
     */
    public void subscribe(String eventType, Consumer<Object> listener) {
        eventListeners.put(eventType, listener);
    }

    /**
     * Unsubscribes a listener from an event type.
     * @param eventType The type of event to unsubscribe from.
     */
    public void publish(String eventType, Object event) {
        Consumer<Object> listener = eventListeners.get(eventType);
        if (listener != null) {
            listener.accept(event);
        }
    }
}