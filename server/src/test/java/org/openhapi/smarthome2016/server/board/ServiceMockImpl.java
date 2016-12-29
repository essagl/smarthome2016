package org.openhapi.smarthome2016.server.board;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import java.io.IOException;

/**
 * Created by ulrich.metzger on 02.01.2017.
 */
public class ServiceMockImpl implements ServiceInterface{
    @Override
    public double getIndoorTemp() throws IOException {
        return 20.90;
    }

    @Override
    public double getOutdoorTemp() throws IOException {
        return 2.25;
    }

    @Override
    public double getHumidity() throws IOException {
        return 45.50;
    }

    @Override
    public GpioPinDigitalInput getButton1() {
        return null;
    }

    @Override
    public GpioPinDigitalInput getButton2() {
        return null;
    }

    @Override
    public GpioPinDigitalInput getButton3() {
        return null;
    }

    @Override
    public GpioPinDigitalOutput getRedLed() {
        return null;
    }

    @Override
    public GpioPinDigitalOutput getGreenLed() {
        return null;
    }

    @Override
    public void setReverenceVoltage(double referenceVoltage) {

    }

    @Override
    public double getReverenceVoltage() {
        return 3.30;
    }

    @Override
    public void setMessage(String message, int displayRow) {

    }

    @Override
    public void setLcdBackLightOFF() {

    }

    @Override
    public void setLcdBackLightON() {

    }
}
