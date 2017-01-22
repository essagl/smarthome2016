package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;
import org.openhpi.smarthome2016.ServiceInterface;
import org.openhpi.smarthome2016.gpio.GPIO;
import org.openhpi.smarthome2016.gpio.PinStateChangedEvent;
import org.openhpi.smarthome2016.gpio.PinStateListener;
import org.openhpi.smarthome2016.lcd.GE_C1602B;
import org.openhpi.smarthome2016.spi.MCP3008;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.openhpi.smarthome2016.lcd.GE_C1602B.clear;

/**
 * Created by essagl on 02.01.2017.
 */
public class ServiceImpl implements ServiceInterface {
    // implement listener to abstract from Pi4J implementation
    private List<PinStateListener> listeners = new ArrayList<PinStateListener>();
    private GpioController gpio;

    @Override
    public void addListener(PinStateListener toAdd) {
        listeners.add(toAdd);
    }

    @Override
    public void setupGPIO() {
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        gpio = GpioFactory.getInstance();
        setupPins(gpio);
    }

    @Override
    public void shutdownGPIO() {
        gpio.removeAllListeners();
        gpio.unprovisionPin(GPIO.getButton1(),GPIO.getButton2(),GPIO.getSwitch1());
        gpio.shutdown();
    }

    @Override
    public double getIndoorTemp() throws IOException {
        return MCP3008.getIndoorTemperature();
    }

    @Override
    public double getOutdoorTemp() throws IOException {
        return MCP3008.getOutdoorTemperature();
    }

    @Override
    public double getHumidity() throws IOException {
        return MCP3008.getRelativeHumidity();
    }

    @Override
    public boolean isButton1Pressed() {
        return GPIO.getButton1().isLow();
    }
    @Override
    public boolean isButton2Pressed() {
        return GPIO.getButton2().isLow();
    }

    @Override
    public boolean isSwitch1Open() {
        return GPIO.getSwitch1().isLow();
    }

    @Override
    public void setRedLedOn() {
        GPIO.getLedRED().high();
    }

    @Override
    public void setRedLedOFF() {GPIO.getLedRED().low();
    }

    @Override
    public boolean isRedLedOn() {
        return GPIO.getLedRED().isHigh();
    }

    @Override
    public void setGreenLedOn() {
        GPIO.getLedGREEN().high();
    }

    @Override
    public void setGreenLedOff() {GPIO.getLedGREEN().low();
    }

    @Override
    public boolean isGreenLedOn() {
        return GPIO.getLedGREEN().isHigh();
    }


    @Override
    public void setReverenceVoltage(double referenceVoltage) {
        MCP3008.setReferenceVoltage(referenceVoltage);
    }

    @Override
    public double getReverenceVoltage() {
        return MCP3008.getReferenceVoltage();
    }

    @Override
    public void setMessage(String message, int displayRow) {
        GE_C1602B.writeln(displayRow,message);
    }

    @Override
    public void setLcdBackLightOFF(){
        GE_C1602B.setBacklightOff();
    }

    @Override
    public void setLcdBackLightON(){
        GE_C1602B.setBacklightOff();
    }

    @Override
    public void clearDisplay() {
        // clear LCD
        clear();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private  GpioPinDigitalInput[] setupPins(GpioController gpio) {
        // provision gpio pins as input pins with its internal pull up resistor enabled
        final GpioPinDigitalInput myButtons[] = {
                GPIO.getButton1(),
                GPIO.getButton2(),
                GPIO.getSwitch1()
        };

        // listen on GPIO Pin State and inform all listeners about changed state
        gpio.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {

            PinStateChangedEvent pinStateChangedEvent =
                    new PinStateChangedEvent(
                            event.getSource(),
                            event.getPin().getName(),
                            event.getState() == com.pi4j.io.gpio.PinState.HIGH? org.openhpi.smarthome2016.gpio.PinState.HIGH: org.openhpi.smarthome2016.gpio.PinState.LOW);


            for (PinStateListener hl : listeners){
                hl.handlePinStateChangedEvent(pinStateChangedEvent);
            }

        }, myButtons);
        return myButtons;
    }
}
