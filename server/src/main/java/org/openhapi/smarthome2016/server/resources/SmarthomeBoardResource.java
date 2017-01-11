package org.openhapi.smarthome2016.server.resources;

import com.codahale.metrics.annotation.Timed;
import org.openhapi.smarthome2016.server.api.MessuredValues;
import org.openhapi.smarthome2016.server.api.Thermometer;
import org.openhapi.smarthome2016.server.board.ServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Path("/board")
@Produces(MediaType.APPLICATION_JSON)
public class SmarthomeBoardResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmarthomeBoardResource.class);

    private final ServiceInterface service;
    private final AtomicLong counter;

    public SmarthomeBoardResource(ServiceInterface service) {
         this.counter = new AtomicLong();
         this.service = service;
    }

    @GET
    @Timed(name = "get-requests")
    public MessuredValues receiveAllData() throws IOException {
        MessuredValues messuredValues = new MessuredValues(counter.incrementAndGet(),
                service.getIndoorTemp(),
                service.getOutdoorTemp(),
                service.getHumidity());

        return messuredValues;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/thermometer")
    public String startThermometer() {
        try {
            Thermometer.displayTemperature();
            return "Thermometer started";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error while starting thermometer";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/thermometer/stop")
    public String stopThermometer() {
        Thermometer.stop();
        return "Thermometer stopped";
     }
}
