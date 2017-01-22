package org.openhapi.smarthome2016.server.resources;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.openhapi.smarthome2016.server.api.MessuredValues;
import org.openhapi.smarthome2016.server.api.RoomClimate;
import org.openhpi.smarthome2016.CircuitBoard;
import org.openhpi.smarthome2016.ServiceInterface;
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
    private CircuitBoard board;

    public SmarthomeBoardResource(ServiceInterface service) {
         this.counter = new AtomicLong();
         this.service = service;
         this.board = new CircuitBoard(service);
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roomClimate")
    @ApiOperation(value = "Room Climate", notes = "Get result for room climate " +
            "calculation from circuit board", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "String message from board display " +
                    "and state of the room climate"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public RoomClimate isRoomClimateOK() {
            RoomClimate roomClimate =
                    new RoomClimate(board.getRoomClimateInfo(),board.isRoomClimateOK());
           return roomClimate;
    }

}
