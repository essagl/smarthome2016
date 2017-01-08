package org.openhapi.smarthome2016.server;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.openhapi.smarthome2016.server.auth.ExampleAuthenticator;
import org.openhapi.smarthome2016.server.auth.ExampleAuthorizer;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;
import org.openhapi.smarthome2016.server.resources.ProtectedResource;
import org.openhapi.smarthome2016.server.resources.SmarthomeBoardResource;
import org.openhapi.smarthome2016.server.resources.UserResource;

public class SmarthomeApplication extends Application<SmarthomeConfiguration> {

    private final HibernateBundle<SmarthomeConfiguration> hibernateBundle =
            new HibernateBundle<SmarthomeConfiguration>(User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(SmarthomeConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };


    public static void main(String[] args) throws Exception {
        new SmarthomeApplication().run(args);
    }


    @Override
    public String getName() {
        return "smarthome";
    }

    @Override
    public void initialize(Bootstrap<SmarthomeConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(hibernateBundle);
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

        final UserDAO dao = new UserDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new UserResource(dao));
    }
}
