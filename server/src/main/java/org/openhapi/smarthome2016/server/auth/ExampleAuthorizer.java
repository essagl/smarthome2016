package org.openhapi.smarthome2016.server.auth;


import io.dropwizard.auth.Authorizer;
import org.openhapi.smarthome2016.server.core.User;


public class ExampleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
