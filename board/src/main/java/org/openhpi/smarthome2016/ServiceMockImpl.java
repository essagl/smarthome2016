package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.openhpi.smarthome2016.ServiceInterface;
import org.openhpi.smarthome2016.gpio.PinStateListener;

import java.io.IOException;

/**
 * Created by ulrich.metzger on 02.01.2017.
 */
public class ServiceMockImpl implements ServiceInterface {
    @Override
    public void setupGPIO() {
        System.out.println("GPIO setup");
    }

    @Override
    public void shutdownGPIO() {
        System.out.println("GPIO shutdown");
    }

    @Override
    public void addListener(PinStateListener pinStateListener) {

    }

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
    public boolean isButton1Pressed() {
        return false;
    }

    @Override
    public boolean isButton2Pressed() {
        return false;
    }

    @Override
    public boolean isSwitch1Open() {
        return false;
    }

    @Override
    public void setRedLedOn() {
        System.out.println("red LED ON");
    }

    @Override
    public void setRedLedOFF() {
        System.out.println("Red LED OFF");
    }

    @Override
    public boolean isRedLedOn() {
        return false;
    }

    @Override
    public void setGreenLedOn() {
        System.out.println("Green LED ON");
    }

    @Override
    public void setGreenLedOff() {
        System.out.println("Green LED OFF");
    }

    @Override
    public boolean isGreenLedOn() {
        return false;
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
        System.out.println(message);
    }

    @Override
    public void setLcdBackLightOFF() {
        System.out.println("LCDBackLight is OFF");
    }

    @Override
    public void setLcdBackLightON() {
        System.out.println("LCDBackLight is ON");
    }

    @Override
    public void clearDisplay() {
        System.out.println("LCD cleared");
    }
}
