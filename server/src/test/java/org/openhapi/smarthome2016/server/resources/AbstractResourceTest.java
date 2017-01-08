package org.openhapi.smarthome2016.server.resources;

import com.google.common.io.BaseEncoding;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import org.openhapi.smarthome2016.server.auth.ExampleAuthenticator;
import org.openhapi.smarthome2016.server.auth.ExampleAuthorizer;
import org.openhapi.smarthome2016.server.core.User;

import javax.ws.rs.client.WebTarget;

/**
 * Created by ulrich on 08.01.17.
 */
public class AbstractResourceTest {
    protected static final BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER =
            new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new ExampleAuthenticator())
                    .setAuthorizer(new ExampleAuthorizer())
                    .setPrefix("Basic")
                    .setRealm("SUPER SECRET STUFF")
                    .buildAuthFilter();

    protected String getAuthorizationHeaderValue(String username, String password) {
        String userPassword = username+":"+password;
        String encodedCredentials = BaseEncoding.base64().encode(userPassword.getBytes());
        return "Basic "+encodedCredentials;
    }


}
