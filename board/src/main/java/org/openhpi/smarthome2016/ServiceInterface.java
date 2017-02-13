package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.openhpi.smarthome2016.gpio.PinStateListener;

import java.io.IOException;

/**
 * Created by essagl on 02.01.2017.
 * The Service Interface to interact with the board hardware
 */
public interface ServiceInterface {

    /**
     * initialise the hardware
     */
    void setupBoard();

    /**
     * shutdown the hardware
     */
    void shutdownBoard();

    /**
     * A Pin State listener gets informed when buttons are pressed or switch changes state.
     * @param pinStateListener
     */
    void addListener(PinStateListener pinStateListener);

    double getIndoorTemp() throws IOException;

    double getOutdoorTemp() throws IOException;

    double getHumidity() throws IOException;

    /**
     * Onboard button 1
     */
    boolean isButton1Pressed();

    /**
     * Onboard button 2
     */
    boolean isButton2Pressed();

    /**
    * Door sensor
    */

    boolean isSwitch1Open();


    /**
     * red LED
     */
    void setRedLedOn();
    void setRedLedOFF();
    boolean isRedLedOn();
    /**
     * green LED
     */
    void setGreenLedOn();
    void setGreenLedOff();
    boolean isGreenLedOn();
    /**
     * adjust the reference voltage for calibration
     * @param referenceVoltage
     */
    void setReverenceVoltage(double referenceVoltage);

    /**
     * get the stored reference voltage
     */
    double getReverenceVoltage();

    /**
     * display a message
     * @param message the message
     * @param displayRow row 0 or row 1
     */
    void setMessage(String message,int displayRow);

    void setLcdBackLightOFF();

    void setLcdBackLightON();

    void clearDisplay();
}
