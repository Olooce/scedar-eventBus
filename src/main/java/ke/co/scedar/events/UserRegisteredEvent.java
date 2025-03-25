package ke.co.scedar.events;

import java.io.Serializable;

/**
 * scedar-eventBus-POC (ke.co.scedar.events)
 * Created by: oloo
 * On: 19/03/2025. 22:22
 * Description:
 **/


public class UserRegisteredEvent implements Serializable {
    private final String userId;
    private final String email;

    public UserRegisteredEvent(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "UserRegisteredEvent{userId='" + userId + "', email='" + email + "'}";
    }
}
