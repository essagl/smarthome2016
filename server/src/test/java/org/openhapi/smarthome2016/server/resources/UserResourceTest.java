package org.openhapi.smarthome2016.server.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest extends AbstractResourceTest {
    private static final UserDAO USER_DAO = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new UserResource(USER_DAO))

            .build();

    @Captor
    private ArgumentCaptor<User> userCaptor;
    private User user;
    private User admin;

    @Before
    public void setUp() {
        user = new User("User","secret", "USER");
        admin = new User("Admin","secret", "USER,ADMIN");
    }

    @After
    public void tearDown() {
        reset(USER_DAO);
    }

    @Test
    public void createPersonTest() throws JsonProcessingException {
        String authHeaderValue = getAuthorizationHeaderValue("admin","secret");

        when(USER_DAO.findByName(contains("Admin"))).thenReturn(Optional.of(admin));
        when(USER_DAO.createOrUpdate(any(User.class))).thenReturn(user);
        final Response response = target("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(USER_DAO).createOrUpdate(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @Test
    public void listPeopleTest() throws Exception {
        String authHeaderValue = getAuthorizationHeaderValue("admin","secret");
        final ImmutableList<User> people = ImmutableList.of(user);
        when(USER_DAO.findByName("admin")).thenReturn(Optional.of(admin));
        when(USER_DAO.findAll()).thenReturn(people);

        final List<User> response = target("/user/list")
                .request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(new GenericType<List<User>>() {
            });

        verify(USER_DAO).findAll();
        assertThat(response).containsAll(people);
    }


    @Test
    public void getUserTest() throws Exception {
        String authHeaderValue = getAuthorizationHeaderValue("admin","secret");
        User hans = new User("hans","secret", "");
        when(USER_DAO.findByName("admin")).thenReturn(Optional.of(admin));
        when(USER_DAO.findByName("hans")).thenReturn(Optional.of(hans));

        final User found = target("/user/hans")
                .request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(User.class);

        assertThat(found).isEqualTo(hans);

    }

    /**
     * test that an unauthorized user can not access the /user URL to search by username
     * @throws Exception
     */
    @Test (expected=javax.ws.rs.ForbiddenException.class)
    public void UnauthorizedGetUserTest() throws Exception {
        String authHeaderValue = getAuthorizationHeaderValue("user","secret");
        when(USER_DAO.findByName("user")).thenReturn(Optional.of(user));

         target("/user/hans")
                .request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(User.class);



    }




    /**
     * Creates a web target to be sent to the resource under testing.
     *
     * @param path relative path (from tested application base URI) this web target should point to.
     * @return the created JAX-RS web target.
     */
    public WebTarget target(String path) {
        return UserResourceTest.RULE.getJerseyTest().target(path);
    }
}
