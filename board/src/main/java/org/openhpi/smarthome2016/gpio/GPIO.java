package org.openhpi.smarthome2016.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Created by essagl on 02.01.2017.
 */
public class GPIO {

    final static GpioController gpio;
    final static GpioPinDigitalOutput ledRED ;
    final static GpioPinDigitalOutput ledGREEN;
    final static GpioPinDigitalInput button1;
    final static GpioPinDigitalInput button2;
    final static GpioPinDigitalInput switch1;

    private static final String BUTTON2_NAME = "button2";
    private static final String BUTTON1_NAME = "button1";
    private static final String SWITCH1_NAME = "switch1";
    static{
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        gpio = GpioFactory.getInstance();

        // provision gpio pin #01 & #03 as an output pins and blink
        ledGREEN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        ledRED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled

        button1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, BUTTON1_NAME,PinPullResistance.PULL_DOWN);
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        button2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, BUTTON2_NAME,PinPullResistance.PULL_DOWN);
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        switch1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, SWITCH1_NAME,PinPullResistance.PULL_DOWN);


    }

    public static GpioPinDigitalOutput getLedRED(){
        return ledRED;
    }

    public static GpioPinDigitalOutput getLedGREEN(){
        return ledGREEN;
    }

    public static GpioPinDigitalInput getButton1(){
        return button1;
    }

    public static GpioPinDigitalInput getButton2(){
        return button2;
    }

    public static GpioPinDigitalInput getSwitch1(){
        return switch1;
    }

    public static String getButton1Name() {
        return BUTTON1_NAME;
    }

    public static String getButton2Name() {
        return BUTTON2_NAME;
    }

    public static String getSwitch1Name() {
        return SWITCH1_NAME;
    }
}
