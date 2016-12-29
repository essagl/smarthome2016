package org.openhapi.smarthome2016.server.board;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import java.io.IOException;

/**
 * Created by essagl on 02.01.2017.
 */
public interface ServiceInterface {

    double getIndoorTemp() throws IOException;

    double getOutdoorTemp() throws IOException;

    double getHumidity() throws IOException;

    /**
     * Onboard button 1
     */
    GpioPinDigitalInput getButton1();

    /**
     * Onboard button 2
     */
    GpioPinDigitalInput getButton2();

    /**
    * Door sensor
    */
    GpioPinDigitalInput getButton3();

    /**
     * red LED
     */
    GpioPinDigitalOutput getRedLed();

    /**
     * green LED
     */
    GpioPinDigitalOutput getGreenLed();

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
}
