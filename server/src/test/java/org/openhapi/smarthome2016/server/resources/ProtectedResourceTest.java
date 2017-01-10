package org.openhapi.smarthome2016.server.resources;


import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.ClassRule;
import org.junit.Test;
import org.openhapi.smarthome2016.server.core.User;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.when;

public class ProtectedResourceTest extends AbstractResourceTest{


    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(ProtectedResource.class)
            .build();

    @Test
    public void testProtectedEndpoint() {
        when(USER_DAO.findByNameAndPassword("user","secret")).thenReturn(Optional.of(user));
        String authHeaderValue = getAuthorizationHeaderValue("user","secret");
        String secret = target("/protected").request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(String.class);
        assertThat(secret).startsWith("Hey there, User. You know the secret!");
    }



    @Test
    public void testProtectedEndpointNoCredentials401() {
        try {
            target("/protected").request()
                .get(String.class);
            failBecauseExceptionWasNotThrown(NotAuthorizedException.class);
        } catch (NotAuthorizedException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
            assertThat(e.getResponse().getHeaders().get(HttpHeaders.WWW_AUTHENTICATE))
                    .containsOnly("Basic realm=\"SUPER SECRET STUFF\"");
        }

    }

    @Test
    public void testProtectedEndpointBadCredentials401() {
        when(USER_DAO.findByNameAndPassword("user","secret")).thenReturn(Optional.empty());
        String authHeaderValue = getAuthorizationHeaderValue("user","secret");

        try {
            target("/protected").request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(String.class);
            failBecauseExceptionWasNotThrown(NotAuthorizedException.class);
        } catch (NotAuthorizedException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
            assertThat(e.getResponse().getHeaders().get(HttpHeaders.WWW_AUTHENTICATE))
                .containsOnly("Basic realm=\"SUPER SECRET STUFF\"");
        }

    }

    @Test
    public void testProtectedAdminEndpoint() {
        when(USER_DAO.findByNameAndPassword("admin","secret")).thenReturn(Optional.of(admin));
        String authHeaderValue = getAuthorizationHeaderValue("admin","secret");
        String secret = target("/protected/admin").request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .get(String.class);
        assertThat(secret).startsWith("Hey there, Admin. It looks like you are an admin.");
    }

    @Test
    public void testProtectedAdminEndpointPrincipalIsNotAuthorized403() {
        when(USER_DAO.findByNameAndPassword("user","secret")).thenReturn(Optional.of(user));
        String authHeaderValue = getAuthorizationHeaderValue("user","secret");
        try {
            target("/protected/admin").request()
                    .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                    .get(String.class);
            failBecauseExceptionWasNotThrown(ForbiddenException.class);
        } catch (ForbiddenException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(403);
        }
    }

    /**
     * Creates a web target to be sent to the resource under testing.
     *
     * @param path relative path (from tested application base URI) this web target should point to.
     * @return the created JAX-RS web target.
     */
    public WebTarget target(String path) {
        return ProtectedResourceTest.RULE.getJerseyTest().target(path);
    }
}
