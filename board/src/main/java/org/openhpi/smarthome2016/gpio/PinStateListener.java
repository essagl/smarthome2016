package org.openhpi.smarthome2016.gpio;

import org.openhpi.smarthome2016.gpio.PinStateChangedEvent;

/**
 * Created by ulrich on 20.01.17.
 */
public interface PinStateListener extends java.util.EventListener{
    void handlePinStateChangedEvent(PinStateChangedEvent event);
}
