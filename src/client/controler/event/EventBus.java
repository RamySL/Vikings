package client.controler.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;
    private Map<String, Consumer<Object>> eventListeners = new HashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(String eventType, Consumer<Object> listener) {
        eventListeners.put(eventType, listener);
    }

    public void publish(String eventType, Object event) {
        Consumer<Object> listener = eventListeners.get(eventType);
        if (listener != null) {
            listener.accept(event);
        }
    }
}