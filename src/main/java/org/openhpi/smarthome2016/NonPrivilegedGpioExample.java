package org.openhpi.smarthome2016;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Created by pi on 26.11.16.
 */
public class NonPrivilegedGpioExample {

    /**
     * This example code demonstrates how to use Pi4J with
     * non-privileged access (non-root/sudo) to GPIO pins on
     * the Raspberry Pi.
     *
     * @author Robert Savage
     */


        public static void main(String[] args) throws InterruptedException {

            System.out.println("<--Pi4J--> Non-Privileged GPIO Example ... started.");

            // we can use this utility method to pre-check to determine if
            // privileged access is required on the running system
            if(GpioUtil.isPrivilegedAccessRequired()){
                System.err.println("*****************************************************************");
                System.err.println("Privileged access is required on this system to access GPIO pins!");
                System.err.println("*****************************************************************");
                return;
            }

            // ----------------------
            // ATTENTION
            // ----------------------
            // YOU CANNOT USE ANY HARDWARE PWM OR CLOCK FUNCTIONS WHILE ACCESSING NON-PRIVILEGED GPIO.
            // THIS METHOD MUST BE INVOKED BEFORE CREATING A GPIO CONTROLLER INSTANCE.
            GpioUtil.enableNonPrivilegedAccess();

            // create gpio controller
            final GpioController gpio = GpioFactory.getInstance();

            // ------------
            // OUTPUT PIN
            // ------------

            // provision gpio pin #01 as an output pin and blink it
            final GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);

            // set shutdown state for the output pin (and un-export the pin)
            output.setShutdownOptions(true, PinState.LOW);

            // blink output pin every one second
            output.blink(1000);

            // display info to user
            System.out.println("Pin [" + output.getName() + "] should be blinking/toggling every 1 second.");

            // ------------
            // INPUT PIN
            // ------------

            // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
            final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, PinPullResistance.PULL_DOWN);

            // set shutdown state for the input pin (and un-export the pin)
            input.setShutdownOptions(true);

            // create and register gpio pin listener
            input.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                }
            });

            // display info to user
            System.out.println("You can connect pin [" + input.getName() + "] to +3VDC to capture input state changes.");

            // ----------------
            // WAIT & SHUTDOWN
            // ----------------

            // sleep for 1 minute, then shutdown
            Thread.sleep(60000);

            // stop all GPIO activity/threads by shutting down the GPIO controller
            // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
            gpio.shutdown();

            System.out.println("Exiting NonPrivilegedGpioExample");
        }


}
