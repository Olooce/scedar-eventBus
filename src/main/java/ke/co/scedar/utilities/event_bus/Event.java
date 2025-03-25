package ke.co.scedar.utilities.event_bus;

import java.io.Serializable;

/**
 * scedar-eventBus-POC (ke.co.scedar.utilities.event_bus)
 * Created by: oloo
 * On: 23/03/2025. 23:50
 * Description:
 **/
public class Event<T> implements Serializable {
    private final String type;
    private final T payload;
    private final int priority;

    public Event(String type, T payload, int priority) {
        this.type = type;
        this.payload = payload;
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Event{type='" + type + "', priority=" + priority + ", payload=" + payload + '}';
    }
}
