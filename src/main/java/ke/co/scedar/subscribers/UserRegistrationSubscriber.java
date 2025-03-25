package ke.co.scedar.subscribers;

import ke.co.scedar.events.UserRegisteredEvent;
import ke.co.scedar.utilities.event_bus.Event;
import ke.co.scedar.utilities.event_bus.EventSubscriber;
import ke.co.scedar.utilities.multi_processing.Processor;

/**
 * scedar-eventBus-POC (ke.co.scedar.subscribers)
 * Created by: oloo
 * On: 23/03/2025. 23:55
 * Description:
 **/

public class UserRegistrationSubscriber implements EventSubscriber<UserRegisteredEvent> {

    @Override
    public void onEvent(Event<UserRegisteredEvent> event) {
        Processor.submitTask(() -> processUserRegistration(event));
    }

    private void processUserRegistration(Event<UserRegisteredEvent> event) {
        UserRegisteredEvent payload = event.getPayload();
        System.out.println("[INFO] Processing user registration: " + payload.getUserId());
        try {
            Thread.sleep(2000);
            System.out.println("[SUCCESS] User registered: " + payload.getUserId());
        } catch (InterruptedException e) {
            System.err.println("[ERROR] User registration interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error processing user registration: " + e.getMessage());
        }
    }
}

