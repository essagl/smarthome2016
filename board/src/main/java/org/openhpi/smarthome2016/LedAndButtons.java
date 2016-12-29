package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Created by pi on 25.11.16.
 * Test a red and green LED interaction with button1 and button2.
 */
public class LedAndButtons {
    private LedAndButtons(){}

    /**
     * Start without parameters to make the LED's blink. The red led blinks for 15 sec,  the green LED does not stop
     * blinking. Pressing the button1, change LED blinking of the green LED, pressing button2 restarts blinking the
     * red LED.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO Blink Example ... started.");
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 & #03 as an output pins and blink
        final GpioPinDigitalOutput ledRED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        final GpioPinDigitalOutput ledGREEN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput button1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput button2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        button1.addListener((GpioPinListenerDigital) event -> {
            // when button is pressed, speed up the blink rate on green led
            if(event.getState().isHigh()){
                ledGREEN.blink(200);
            }
            else{
                ledGREEN.blink(1000);
            }
        });
        button2.addListener((GpioPinListenerDigital) event -> {
            // when button is pressed, speed up the blink rate on green led
            if(event.getState().isHigh()){
                // continuously blink the led every 1/2 second for 15 seconds
                ledRED.blink(500, 15000);
            }
            else{
                ledRED.blink(1000,15000);
            }
        });
        // continuously blink the led every 1/2 second for 15 seconds
        ledRED.blink(500, 15000);

        // continuously blink the led every 1 second
        ledGREEN.blink(1000);

        System.out.println(" ... the LED will continue blinking until the program is terminated.");
        System.out.println(" ... PRESS <CTRL-C> TO STOP THE PROGRAM.");

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}
