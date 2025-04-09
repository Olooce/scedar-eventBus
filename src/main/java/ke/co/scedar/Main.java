package ke.co.scedar;

import ke.co.scedar.events.UserRegisteredEvent;
import ke.co.scedar.subscribers.UserRegistrationSubscriber;
import ke.co.scedar.utilities.Logger;
import ke.co.scedar.utilities.event_bus.Event;
import ke.co.scedar.utilities.event_bus.EventBus;
import ke.co.scedar.utilities.multi_processing.Processor;

/**
 * scedar-eventBus-POC (ke.co.scedar)
 * Created by: oloo
 * On: 19/03/2025. 22:18
 * Description:
 **/
public class Main {
    public static void main(String[] args) {
        Processor.init();
        EventBus eventBus = EventBus.getInstance();

        // Subscribe to events
        eventBus.subscribe(UserRegisteredEvent.class, new UserRegistrationSubscriber());

        // Stress test: publish a large number of events
        Logger.info("Publishing events stress test...");
        int numberOfEvents = 100_000; // adjust this number based on how heavy you want the test to be
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfEvents; i++) {
            UserRegisteredEvent payload = new UserRegisteredEvent("user" + i, "user" + i + "@example.com");
            Event<UserRegisteredEvent> event = new Event<>("UserRegistered", payload, 1);
            eventBus.publish(event);
        }

        long endTime = System.currentTimeMillis();
        Logger.info("Published " + numberOfEvents + " events in " + (endTime - startTime) + " ms");

        // Allow some time for background processing of events
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error("Interrupted while sleeping", e);
        }

        // Shutdown event bus and processor to release resources
        eventBus.shutdown();
        Processor.shutdown();
    }
}
