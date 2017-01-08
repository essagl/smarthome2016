package org.openhapi.smarthome2016.server.auth;


import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.openhapi.smarthome2016.server.core.User;

import java.util.Optional;
import java.util.Set;

public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {
    /**
     * Valid users with mapping user -> roles
     */
    private static final Set<User> VALID_USERS = ImmutableSet.of(
            new User("guest"),
            new User("user","secret","USER"),
            new User("admin","secret","USER,ADMIN")
    );

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        User user = new User(credentials.getUsername(),credentials.getPassword());
        if (VALID_USERS.contains(user)) {
            return Optional.of(VALID_USERS.stream().filter(u -> u.equals(user)).findFirst().get());
        }
        return Optional.empty();
    }
}