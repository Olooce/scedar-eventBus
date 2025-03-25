package ke.co.scedar;

import ke.co.scedar.events.UserRegisteredEvent;
import ke.co.scedar.subscribers.UserRegistrationSubscriber;
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
        EventBus bus = EventBus.getInstance();
        bus.subscribe(UserRegisteredEvent.class, new UserRegistrationSubscriber());

        // Publish events
        System.out.println("Publishing events...");
        UserRegisteredEvent registrationPayload = new UserRegisteredEvent("user123", "user@example.com");
        Event<UserRegisteredEvent> registrationEvent = new Event<>("UserRegistered", registrationPayload, 1);
        EventBus.getInstance().publish(registrationEvent);


        // Simulate delay for background processing
        try { Thread.sleep(10000); } catch (InterruptedException ignored) {}

        // Shutdown event bus to release resources
        eventBus.shutdown();
        Processor.shutdown();
    }
}
