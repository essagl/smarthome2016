package org.openhapi.smarthome2016.server;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.openhapi.smarthome2016.server.auth.ExampleAuthenticator;
import org.openhapi.smarthome2016.server.auth.ExampleAuthorizer;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.resources.ProtectedResource;
import org.openhapi.smarthome2016.server.resources.SmarthomeBoardResource;

public class SmarthomeApplication extends Application<SmarthomeConfiguration> {
    public static void main(String[] args) throws Exception {
        new SmarthomeApplication().run(args);
    }


    @Override
    public String getName() {
        return "smarthome";
    }

    @Override
    public void initialize(Bootstrap<SmarthomeConfiguration> bootstrap) {

    }

    @Override
    public void run(SmarthomeConfiguration configuration, Environment environment) {

        try {
            environment.jersey().register(new SmarthomeBoardResource(configuration.getBoardService()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        environment.jersey().register(new ProtectedResource());
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new ExampleAuthenticator())
                .setAuthorizer(new ExampleAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
