package org.openhapi.smarthome2016.server.resources;

import com.google.common.io.BaseEncoding;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import org.junit.After;
import org.junit.Before;
import org.openhapi.smarthome2016.server.auth.ExampleAuthenticator;
import org.openhapi.smarthome2016.server.auth.ExampleAuthorizer;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * Created by ulrich on 08.01.17.
 */
public class AbstractResourceTest {

    protected static final UserDAO USER_DAO = mock(UserDAO.class);

    protected static final BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER =
            new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new ExampleAuthenticator(USER_DAO))
                    .setAuthorizer(new ExampleAuthorizer())
                    .setPrefix("Basic")
                    .setRealm("SUPER SECRET STUFF")
                    .buildAuthFilter();

    protected User user;
    protected User admin;



    public static String getAuthorizationHeaderValue(String username, String password) {
        String userPassword = username+":"+password;
        String encodedCredentials = BaseEncoding.base64().encode(userPassword.getBytes());
        return "Basic "+encodedCredentials;
    }


    @Before
    public void setUp() {
        user = new User("User","secret", "USER");
        admin = new User("Admin","secret", "USER,ADMIN");
    }

    @After
    public void tearDown() {
        reset(USER_DAO);
    }

}
