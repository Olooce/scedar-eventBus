package ke.co.scedar.subscribers;

import ke.co.scedar.events.UserRegisteredEvent;
import ke.co.scedar.utilities.Logger;
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
        Logger.info("Processing user registration: " + payload.getUserId());
        try {
            Thread.sleep(2000);
            Logger.info("User registered: " + payload.getUserId());
        } catch (InterruptedException e) {
            Logger.error("User registration interrupted for " + payload.getUserId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Logger.error("Unexpected error processing registration for " + payload.getUserId(), e);
        }
    }
}
