package ke.co.scedar.utilities.event_bus;

/**
 * scedar-eventBus-POC (ke.co.scedar.utilities.event_bus)
 * Created by: oloo
 * On: 23/03/2025. 23:51
 * Description:
 **/

public interface EventSubscriber<T> {
    void onEvent(Event<T> event);
}

