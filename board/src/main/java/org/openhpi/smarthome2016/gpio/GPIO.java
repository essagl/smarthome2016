package org.openhpi.smarthome2016.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by essagl on 02.01.2017.
 */
public class GPIO {

    final static GpioController gpio;
    final static GpioPinDigitalOutput ledRED;
    final static GpioPinDigitalOutput ledGREEN;
    final static GpioPinDigitalInput button1;
    final static GpioPinDigitalInput button2;


    static{
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        gpio = GpioFactory.getInstance();

        // provision gpio pin #01 & #03 as an output pins and blink
        ledRED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        ledGREEN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        button1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        button2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

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

    public static GpioPinDigitalInput getButton3(){
        throw new NotImplementedException();
    }
}