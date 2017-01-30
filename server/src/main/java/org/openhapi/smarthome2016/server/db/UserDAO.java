package org.openhapi.smarthome2016.server.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.openhapi.smarthome2016.server.core.User;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Optional<User> findByName(String name) {
        return Optional.ofNullable(
                uniqueResult(
                        namedQuery("org.openhapi.smarthome2016.server.core.User.findByName").setParameter("name",name)
                )
        );
    }

    public Optional<User> findByNameAndPassword(String name, String password) {
        return Optional.ofNullable(
                uniqueResult(
                        namedQuery("org.openhapi.smarthome2016.server.core.User.findByNameAndPassword")
                                .setParameter("name",name).setParameter("password",password)
                )
        );
    }

    /**
     * Create or update a user. When creating a new user the id propertiy must be null.
     * @param user
     * @return the user or null.
     */
    public User createOrUpdate(User user) {
        return persist(user);
    }


    public List<User> findAll() {
        return list(namedQuery("org.openhapi.smarthome2016.server.core.User.findAllNewerThan"));
    }
}
