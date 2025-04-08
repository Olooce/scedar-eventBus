package ke.co.scedar.utilities.event_bus;

import ke.co.scedar.utilities.Logger;

import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final Map<Class<?>, BlockingQueue<Event<?>>> eventCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<EventSubscriber<?>>> subscribers = new ConcurrentHashMap<>();
    private static boolean persistUnprocessedEventsOnShutdown = false;
    private static final File STORAGE_DIR;

    static {
        STORAGE_DIR = new File("event_storage");
        if (!STORAGE_DIR.exists()) STORAGE_DIR.mkdirs();
    }

    private EventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public static void init(boolean persistOnShutdown) {
        persistUnprocessedEventsOnShutdown = persistOnShutdown;
    }

    public <T extends Serializable> void subscribe(Class<T> eventType, EventSubscriber<T> subscriber) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(subscriber);
        eventCache.putIfAbsent(eventType, new LinkedBlockingQueue<>());
    }

    public <T extends Serializable> void publish(Event<T> event) {
        Class<?> key = event.getPayload().getClass();
        eventCache.computeIfAbsent(key, k -> new LinkedBlockingQueue<>()).offer(event);

        List<EventSubscriber<?>> subs = subscribers.get(key);
        if (subs != null) {
            for (EventSubscriber subscriber : subs) {
                subscriber.onEvent(event);
            }
        }
    }

    public <T extends Serializable> Event<T> retrieveEvent(Class<T> eventType) throws InterruptedException {
        BlockingQueue<Event<?>> queue = eventCache.get(eventType);
        if (queue == null) return null;
        return (Event<T>) queue.take();
    }

    public void shutdown() {
        if (persistUnprocessedEventsOnShutdown) {
            Logger.info("Persisting unprocessed events to disk...");
            saveUnprocessedEvents();
        }
    }

    private void saveUnprocessedEvents() {
        for (Map.Entry<Class<?>, BlockingQueue<Event<?>>> entry : eventCache.entrySet()) {
            File file = new File(STORAGE_DIR, entry.getKey().getSimpleName() + ".dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                while (!entry.getValue().isEmpty()) {
                    oos.writeObject(entry.getValue().poll());
                }
                Logger.info("Persisted events for: " + entry.getKey().getSimpleName());
            } catch (IOException e) {
                Logger.error("Failed to persist events for " + entry.getKey().getSimpleName(), e);
            }
        }
    }
}
