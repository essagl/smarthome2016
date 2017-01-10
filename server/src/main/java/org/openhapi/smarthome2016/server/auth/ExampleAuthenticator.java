package org.openhapi.smarthome2016.server.auth;


import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import java.util.Optional;

public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UserDAO userDAO;

    public ExampleAuthenticator(SessionFactory sessionFactory) {
         userDAO = new UserDAO(sessionFactory);
    }

    public ExampleAuthenticator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return userDAO.findByNameAndPassword(credentials.getUsername(),credentials.getPassword());
    }
}