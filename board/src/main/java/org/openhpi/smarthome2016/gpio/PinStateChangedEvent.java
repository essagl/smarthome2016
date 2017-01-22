package org.openhpi.smarthome2016.gpio;

import org.openhpi.smarthome2016.gpio.PinState;

/**
 * Created by ulrich on 20.01.17.
 */
public class PinStateChangedEvent {
    static final long serialVersionUID = 1L;
    private final PinState state;


    /**
     * Default event constructor
     *
     * @param obj    Ignore this parameter
     * @param pinName  pin Name
     * @param state  New pin state.
     */
    public PinStateChangedEvent(Object obj, String pinName, PinState state) {

        this.state = state;


    }

    /**
     * Get the new pin state raised in this event.
     *
     * @return GPIO pin state (HIGH, LOW)
     */
    public PinState getState() {
        return state;
    }


}
