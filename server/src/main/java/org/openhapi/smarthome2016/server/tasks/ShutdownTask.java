package org.openhapi.smarthome2016.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import org.openhpi.smarthome2016.CircuitBoard;

import java.io.PrintWriter;

/**
 * Created by ulrich on 23.01.17.
 */
public class ShutdownTask extends Task {
    private CircuitBoard board;

    public ShutdownTask (CircuitBoard board) {
        super("shutdown");
        this.board = board;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
         board.shutdown();
    }
}