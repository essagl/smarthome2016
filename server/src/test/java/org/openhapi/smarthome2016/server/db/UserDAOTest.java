package org.openhapi.smarthome2016.server.db;

import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openhapi.smarthome2016.server.core.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDAOTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
        .addEntityClass(User.class)
        .build();

    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = new UserDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createPerson() {
        final User jeff = daoTestRule.inTransaction(() -> userDAO.createOrUpdate(new User("Jeff", "secret","USER,ADMIN")));
        assertThat(jeff.getId()).isGreaterThan(0);
        assertThat(jeff.getName()).isEqualTo("Jeff");
        assertThat(jeff.getPassword()).isEqualTo("secret");
        assertThat(userDAO.findById(jeff.getId())).isEqualTo(Optional.of(jeff));

        assertThat(userDAO.findByName(jeff.getName())).isEqualTo(Optional.of(jeff));
    }

    @Test
    public void findAll() {
        daoTestRule.inTransaction(() -> {
            userDAO.createOrUpdate(new User("Jeff", "The plumber"));
            userDAO.createOrUpdate(new User("Jim", "The cook"));
            userDAO.createOrUpdate(new User("Randy", "The watchman"));
        });

        final List<User> users = userDAO.findAll();
        assertThat(users).extracting("name").containsOnly("Jeff", "Jim", "Randy");
        assertThat(users).extracting("password").containsOnly("The plumber", "The cook", "The watchman");
    }


}
