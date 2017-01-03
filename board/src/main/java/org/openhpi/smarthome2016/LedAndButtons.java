package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;
import org.openhpi.smarthome2016.gpio.GPIO;

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
        final GpioPinDigitalOutput ledRED = GPIO.getLedRED();
        final GpioPinDigitalOutput ledGREEN = GPIO.getLedGREEN();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput button1 = GPIO.getButton1();
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput button2 = GPIO.getButton2();

        final GpioPinDigitalInput switch1 = GPIO.getSwitch1();

        // create and register gpio pin listener
        button1.addListener((GpioPinListenerDigital) event -> {
            // when button is pressed, speed up the blink rate on green led
            if(event.getState().isHigh()){
                ledGREEN.blink(200);
            }
            else{
                ledGREEN.blink(2000);
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
        switch1.addListener((GpioPinListenerDigital) event -> {
            // when button is pressed, speed up the blink rate on green led
            if(event.getState().isHigh()){

                System.out.println(" ... Switch1 changed state to CLOSE.");
            }
            else{
                System.out.println(" ... Switch 1 changed state to OPEN.");
            }
        });
        // continuously blink the led every 1/2 second for 15 seconds
        ledRED.blink(500, 15000);

        // continuously blink the led every 1 second
        ledGREEN.blink(1000);
    if (switch1.getState().isHigh()){
        System.out.println(" ... switch1 is closed.");
    }   else {
        System.out.println(" ... switch1 is open.");
    }
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
