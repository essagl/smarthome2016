package org.openhpi.smarthome2016;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;
import org.openhpi.smarthome2016.spi.MCP3008;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;
import static org.openhpi.smarthome2016.lcd.GE_C1602B.*;

/**
 * Display the indoor and outdoor temperature.<br>
 * Press both buttons to exit the program and switch off the LCD background light.
 * Press button2 to decrease reference voltage - button1 to increase reference voltage. (calibration)
 */
public class DisplayTemperature {

    private DisplayTemperature(){}

    public final static int LCD_ROW_1 = 0;
    public final static int LCD_ROW_2 = 1;

    private static boolean displaying = true;

    /**
     * No arguments are needed to run this program.
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String args[]) throws InterruptedException, IOException {

        System.out.println("<-- Openhpi Smarthome --> GPIO 4 bit LCD example program");
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


        // clear LCD
        clear();
        sleep(1000);



        int toggleCounter = 0;
        writeln(LCD_ROW_1, "Temperature C", LCDTextAlignment.ALIGN_CENTER);
        boolean run = true;
        // update time
        while(run) {
            if (displaying) {
                if (toggleCounter < 50) {
                    writeln(LCD_ROW_2, String.format("Indoor: %1$.2f", MCP3008.getIndoorTemperature()), LCDTextAlignment.ALIGN_CENTER);
                } else {
                    writeln(LCD_ROW_2, String.format("Outdoor: %1$.2f", MCP3008.getOutdoorTemperature()), LCDTextAlignment.ALIGN_CENTER);
                }
                sleep(250);
                toggleCounter ++;
                if (toggleCounter > 100){
                    toggleCounter = 0;
                }
                // write time to line 2 on LCD and exit
                if(gpio.isHigh(myButtons)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    writeln(LCD_ROW_1, "By by ....", LCDTextAlignment.ALIGN_CENTER);
                    writeln(LCD_ROW_2, formatter.format(new Date()), LCDTextAlignment.ALIGN_CENTER);
                    sleep(1000);
                    run = false;
                }
            }

        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        setBacklightOff();
        gpio.shutdown();   //<--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }

    private static void stopDisplaying(){
        displaying = false;
    }
    private static void startDisplaying(){
        displaying = true;
    }
}

