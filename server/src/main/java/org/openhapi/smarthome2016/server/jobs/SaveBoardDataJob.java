package org.openhapi.smarthome2016.server.jobs;

import com.google.common.util.concurrent.AbstractScheduledService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import org.joda.time.DateTime;
import org.openhapi.smarthome2016.server.core.BoardData;
import org.openhapi.smarthome2016.server.db.BoardDataDAO;
import org.openhpi.smarthome2016.CircuitBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by ulrich on 27.01.17.
 */
public class SaveBoardDataJob extends AbstractScheduledService implements Managed {
    private final Logger LOGGER = LoggerFactory.getLogger(SaveBoardDataJob.class);
    private Scheduler periodicTask =  AbstractScheduledService.Scheduler.newFixedRateSchedule(1, 60, TimeUnit.MINUTES);
    CircuitBoard board;
    BoardDataDAO boardDataDAO;

    public SaveBoardDataJob(CircuitBoard board, BoardDataDAO boardDataDAO) {
        this.board = board;
        this.boardDataDAO = boardDataDAO;
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
                double outDoorTemp = board.getBoardService().getOutdoorTemp();
                double inDoorTemp = board.getBoardService().getIndoorTemp();
                double humidity = board.getBoardService().getHumidity();
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
}
