package org.openhapi.smarthome2016.server.api;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;
import org.openhpi.smarthome2016.spi.MCP3008;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.openhpi.smarthome2016.lcd.GE_C1602B.*;

/**
 * Display the indoor and outdoor temperature.<br>
 * Press both buttons to switch off the LCD background light.
 * Press button2 to decrease reference voltage - button1 to increase reference voltage. (calibration)
 */
public class Thermometer {

    private Thermometer(){}

    public final static int LCD_ROW_1 = 0;
    public final static int LCD_ROW_2 = 1;

    private static boolean displaying = true;
    private static boolean run = false;

    private static Thread thermometerThread;

    /**
     * No arguments are needed to run this program.
     * @throws InterruptedException
     * @throws IOException
     */

    private static Runnable thermometerTask = () -> {
        run = true;
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();


        // provision gpio pins as input pins with its internal pull up resistor enabled
        final GpioPinDigitalInput myButtons[] = {
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "B1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "B2", PinPullResistance.PULL_UP),
        };

        // create and register gpio pin listener to change reference voltage for calibration
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                stopDisplaying();
                if (event.getPin().getName().equals("B1") && event.getState() == PinState.HIGH) {
                    double vRef = MCP3008.getReferenceVoltage();
                    if (vRef < 3.3 ){
                        vRef = vRef + 0.01;
                        MCP3008.setReferenceVoltage(vRef);
                    }
                }
                if (event.getPin().getName().equals("B2") && event.getState() == PinState.HIGH) {
                    double vRef = MCP3008.getReferenceVoltage();
                    if (vRef > 1.0 ){
                        vRef = vRef - 0.01;
                        MCP3008.setReferenceVoltage(vRef);
                    }
                }

                writeln(LCD_ROW_2,  String.format("vRef: %1$.2f V", MCP3008.getReferenceVoltage()), LCDTextAlignment.ALIGN_CENTER);

                if (event.getState() == PinState.LOW) {
                    startDisplaying();
                }


            }
        }, myButtons);

        clearDisplay();
        setBacklightOn();

        int toggleCounter = 0;
        writeln(LCD_ROW_1, "Temperature C", LCDTextAlignment.ALIGN_CENTER);

        // update time
        while(run) {
            if (displaying) {
               try {
                    if (toggleCounter < 50) {
                        writeln(LCD_ROW_2, String.format("Indoor: %1$.2f", MCP3008.getIndoorTemperature()), LCDTextAlignment.ALIGN_CENTER);
                    } else {
                        writeln(LCD_ROW_2, String.format("Outdoor: %1$.2f", MCP3008.getOutdoorTemperature()), LCDTextAlignment.ALIGN_CENTER);
                    }
                    sleep(250);
                } catch (IOException e) {
                    e.printStackTrace();
                    run = false;
                } catch (InterruptedException e) {
                   e.printStackTrace();
                   run = false;
               }
                toggleCounter ++;
                if (toggleCounter > 100){
                    toggleCounter = 0;
                }
                if(gpio.isHigh(myButtons)) {
                    if (isBacklightOn()){
                        setBacklightOff();
                    } else {
                        setBacklightOn();
                    }

                }
            }

        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        clearDisplay();
        setBacklightOff();
        gpio.removeAllListeners();
        gpio.unprovisionPin(myButtons[0],myButtons[1]);
        gpio.shutdown();   //<--- implement this method call if you wish to terminate the Pi4J GPIO controller
        thermometerThread = null;
    };

    private static void clearDisplay() {
        // clear LCD
        clear();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            run  = false;
        }
    }

    public static void displayTemperature() throws InterruptedException, IOException {
        if (null == thermometerThread){
            thermometerThread = new Thread(thermometerTask);
            thermometerThread.start();
        }
    }

    public static void stop(){
         run = false;
     }

    private static void stopDisplaying(){
        displaying = false;
        clearDisplay();
    }
    private static void startDisplaying(){
        clearDisplay();
        displaying = true;
    }
}

