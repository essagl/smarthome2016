package org.openhapi.smarthome2016.server.resources;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api("/board")
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
    @ApiOperation(value = "Get the sensor values", notes = "Returns indoor,outdoor temperature in " +
            "degree celsius and humidity in percent.", response = MessuredValues.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "MessuredValues") })
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
    @ApiOperation(value = "Thermometer", notes = "Starting the thermometer program", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "-Thermometer started- or -Error while starting thermometer-"),
            @ApiResponse(code = 500, message = "GPIO interface not present or board error")})
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
    @ApiOperation(value = "Thermometer", notes = "Stopping the thermometer program")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thermometer stopped", response = String.class),
            @ApiResponse(code = 500, message = "GPIO interface not present or board error") })
    @Path("/thermometer/stop")
    public String stopThermometer() {
        Thermometer.stop();
        return "Thermometer stopped";
     }
}
