package org.openhapi.smarthome2016.server.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhapi.smarthome2016.server.api.RoomClimate;
import org.openhapi.smarthome2016.server.core.BoardData;
import org.openhapi.smarthome2016.server.db.BoardDataDAO;
import org.openhpi.smarthome2016.CircuitBoard;
import org.openhpi.smarthome2016.ServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Api("/board")
@Path("/board")
@Produces(MediaType.APPLICATION_JSON)
public class SmarthomeBoardResource implements Managed{
    private static final Logger LOGGER = LoggerFactory.getLogger(SmarthomeBoardResource.class);

    private final ServiceInterface service;
    private final AtomicLong counter;
    private CircuitBoard board;
    private BoardDataDAO boardDataDAO;

    public SmarthomeBoardResource(ServiceInterface service,BoardDataDAO boardDataDAO) {
         this.counter = new AtomicLong();
         this.service = service;
         this.board = new CircuitBoard(service);
         this.boardDataDAO = boardDataDAO;
    }

    public CircuitBoard getBoard(){
        return board;
    }


    @GET
    @Timed(name = "get-requests")
    @ApiOperation(value = "Get the sensor values", notes = "Returns indoor,outdoor temperature in " +
            "degree celsius, humidity in percent, external switch state and the result message of " +
            "room climate calculation.", response = BoardData.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Actual board data") })
    public BoardData receiveAllData() throws IOException {
        BoardData messuredValues = new BoardData(DateTime.now(),service.getOutdoorTemp(),
                service.getIndoorTemp(),
                service.getHumidity(),service.isSwitch1Open(),board.getRoomClimateInfo());

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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/memory")
    @ApiOperation(value = "Board memory", notes = "Get all stored data " +
            "from circuit board since the passed date. Pass the date with the given pattern." +
            "IsoDatePattern defaults to yyyyMMdd", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK - Board Data as JSON"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public List<BoardData> getData(@ApiParam(name = "isoDate", value = "The Date value", required = true)
                                       @QueryParam("isoDate") String isoDate,
                                   @ApiParam(name = "isoDatePattern",
                                           value = "The Pattern of the Date value - defaults to yyyyMMdd")
                                   @QueryParam("pattern") String isoDatePattern) {
        if (isoDatePattern == null){
            isoDatePattern ="yyyyMMdd";
        }
        DateTimeFormatter parser1 = DateTimeFormat.forPattern(isoDatePattern);
        DateTime timestamp = DateTime.parse(isoDate,parser1);
        List<BoardData> dataList = boardDataDAO.findAllNewerThan(timestamp);
        return dataList;
    }

    @Override
    public void start() throws Exception {
        // starts with constructor of Board
    }

    @Override
    public void stop() throws Exception {
        board.shutdown();
    }
}
