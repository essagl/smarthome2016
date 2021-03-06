package org.openhapi.smarthome2016.server.jobs;

import com.google.common.util.concurrent.AbstractScheduledService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import org.joda.time.DateTime;
import org.openhapi.smarthome2016.server.core.BoardData;
import org.openhapi.smarthome2016.server.db.BoardDataDAO;
import org.openhpi.smarthome2016.CircuitBoard;
import org.openhpi.smarthome2016.gpio.GPIO;
import org.openhpi.smarthome2016.gpio.PinStateChangedEvent;
import org.openhpi.smarthome2016.gpio.PinStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by essagl on 27.01.17.
 * Starts a background job, that stores the board data every hour in the database,
 * or if switch1 changed it's state
 */
public class SaveBoardDataJob extends AbstractScheduledService implements Managed, PinStateListener {
    private final Logger LOGGER = LoggerFactory.getLogger(SaveBoardDataJob.class);
    private Scheduler periodicTask =  AbstractScheduledService.Scheduler.newFixedRateSchedule(1, 60, TimeUnit.MINUTES);
    CircuitBoard board;
    BoardDataDAO boardDataDAO;

    public SaveBoardDataJob(CircuitBoard board, BoardDataDAO boardDataDAO) {
        this.board = board;
        this.boardDataDAO = boardDataDAO;
        this.board.getBoardService().addListener(this);

    }


    @Override
    @UnitOfWork
    protected void runOneIteration() throws Exception {
        try {
            runJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }

    }

    @Override
    protected Scheduler scheduler() {
        return periodicTask;
    }

    @Override
    public void start() throws Exception {
        if (null != boardDataDAO && null != board) {
            LOGGER.info("starting SaveBoardDataJob");
            this.startAsync().awaitRunning();
        }else {
            LOGGER.error("not initialized!");
        }

    }

    @Override
    public void stop() throws Exception {
        if (null != boardDataDAO && null != board) {
            LOGGER.info("stopping SaveBoardDataJob");
            this.stopAsync().awaitTerminated();
        }else {
            LOGGER.error("not initialized!");
        }

    }

    private void runJob() {
        if (null != boardDataDAO && null != board) {

            try {
                double outDoorTemp = board.getAverageOutdoorTemp();
                double inDoorTemp = board.getAverageIndoorTemp();
                double humidity = board.getAverageHumidity();
                boolean switchOpen = board.getBoardService().isSwitch1Open();
                String message = board.getRoomClimateInfo();
                DateTime datetime = DateTime.now();

                BoardData boardData = new BoardData(datetime,
                        outDoorTemp,
                        inDoorTemp,
                        humidity,
                        switchOpen,
                        message);
                LOGGER.info("saving BoardData: "+boardData);
                boardDataDAO.createOrUpdate(boardData);

            } catch (Exception e) {
                LOGGER.error("Could not run job because "+e.getMessage());
            }

        } else {
            LOGGER.error("not initialized!");
        }
    }

    @Override
    @UnitOfWork
    public void handlePinStateChangedEvent(PinStateChangedEvent event) {
            runJob();
     }
}
