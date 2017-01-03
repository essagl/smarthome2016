package org.openhapi.smarthome2016.server.board;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.openhpi.smarthome2016.gpio.GPIO;
import org.openhpi.smarthome2016.lcd.GE_C1602B;
import org.openhpi.smarthome2016.spi.MCP3008;

import java.io.IOException;

/**
 * Created by essagl on 02.01.2017.
 */
public class ServiceImpl implements ServiceInterface {
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
    public GpioPinDigitalInput getButton1() {
        return GPIO.getButton1();
    }

    @Override
    public GpioPinDigitalInput getButton2() {
        return GPIO.getButton2();
    }

    @Override
    public GpioPinDigitalInput getSwitch1() {
        return GPIO.getSwitch1();
    }

    @Override
    public GpioPinDigitalOutput getRedLed() {
        return GPIO.getLedRED();
    }

    @Override
    public GpioPinDigitalOutput getGreenLed() {
        return GPIO.getLedGREEN();
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
}
