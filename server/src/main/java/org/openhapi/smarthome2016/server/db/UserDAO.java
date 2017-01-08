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

    public User createOrUpdate(User user) {
        return persist(user);
    }


    public List<User> findAll() {
        return list(namedQuery("org.openhapi.smarthome2016.server.core.User.findAll"));
    }
}
