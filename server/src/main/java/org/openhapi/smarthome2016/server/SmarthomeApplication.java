package org.openhapi.smarthome2016.server;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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

    }
}
