package org.openhpi.smarthome2016;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  LcdExample.java
 *
 * This file is based on the one which is part of the Pi4J project.
 * More information about Pi4J project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;
import org.openhpi.smarthome2016.lcd.GE_C1602B;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Test that the LCD of type GE_C1602B installed on the embedded
 * smarthome board used with the openHPI course 2016 works as desired.</p>
 * Pressing the buttons shows the detected action. Pressing both buttons shows the date. *
 */
public class LcdExample {

    private LcdExample(){}

  //  public final static int LCD_ROWS = 2;
    public final static int LCD_ROW_1 = 0;
    public final static int LCD_ROW_2 = 1;
  //  public final static int LCD_COLUMNS = 16;


    /**
     * Show LCD capabilities. No parameters required.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO 4 bit LCD example program");
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // initialize LCD
        //final GE_C1602B lcd = new GE_C1602B(); // LCD data bit 4

        // provision gpio pins as input pins with its internal pull up resistor enabled
        final GpioPinDigitalInput myButtons[] = {
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "B1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "B2", PinPullResistance.PULL_UP),
         };

        // create and register gpio pin listener
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if(event.getState() == PinState.LOW){
                    GE_C1602B.writeln(LCD_ROW_2,  event.getPin().getName() + " RELEASED" , LCDTextAlignment.ALIGN_CENTER);
                }
                else {
                    GE_C1602B.writeln(LCD_ROW_2,  event.getPin().getName() + " PRESSED" , LCDTextAlignment.ALIGN_CENTER);
                }
            }
        }, myButtons);


        // clear LCD
        GE_C1602B.clear();
        Thread.sleep(1000);

        // write line 1 to LCD
        GE_C1602B.write(LCD_ROW_1, "The Pi4J Project");

        // write line 2 to LCD
        GE_C1602B.write(LCD_ROW_2, "----------------");

        // line data replacement
        for(int index = 0; index < 5; index++)
        {
            GE_C1602B.write(LCD_ROW_2, "----------------");
            Thread.sleep(500);
            GE_C1602B.write(LCD_ROW_2, "****************");
            Thread.sleep(500);
        }
        GE_C1602B.write(LCD_ROW_2, "----------------");

        // single character data replacement
        for(int index = 0; index < GE_C1602B.getColumnCount(); index++) {
            GE_C1602B.write(LCD_ROW_2, index, ">");
            if(index > 0)
                GE_C1602B.write(LCD_ROW_2, index - 1, "-");
            Thread.sleep(300);
        }
        for(int index = GE_C1602B.getColumnCount()-1; index >= 0 ; index--) {
            GE_C1602B.write(LCD_ROW_2, index, "<");
            if(index < GE_C1602B.getColumnCount()-1)
                GE_C1602B.write(LCD_ROW_2, index + 1, "-");
            Thread.sleep(300);
        }

        // left alignment, full line data
        GE_C1602B.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        GE_C1602B.writeln(LCD_ROW_2, "<< LEFT");
        Thread.sleep(1000);

        // right alignment, full line data
        GE_C1602B.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        GE_C1602B.writeln(LCD_ROW_2, "RIGHT >>", LCDTextAlignment.ALIGN_RIGHT);
        Thread.sleep(1000);

        // center alignment, full line data
        GE_C1602B.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        GE_C1602B.writeln(LCD_ROW_2, "<< CENTER >>", LCDTextAlignment.ALIGN_CENTER);
        Thread.sleep(1000);

        // mixed alignments, partial line data
        GE_C1602B.write(LCD_ROW_2, "----------------");
        Thread.sleep(500);
        GE_C1602B.write(LCD_ROW_2, "<L>", LCDTextAlignment.ALIGN_LEFT);
        GE_C1602B.write(LCD_ROW_2, "<R>", LCDTextAlignment.ALIGN_RIGHT);
        GE_C1602B.write(LCD_ROW_2, "CC", LCDTextAlignment.ALIGN_CENTER);
        Thread.sleep(3000);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // update time
        while(true) {
            // write time to line 2 on LCD
            if(gpio.isHigh(myButtons)) {
                GE_C1602B.write(LCD_ROW_1, "The Pi4J Project");
                GE_C1602B.writeln(LCD_ROW_2, formatter.format(new Date()), LCDTextAlignment.ALIGN_CENTER);
            }
            Thread.sleep(1000);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        //gpio.shutdown();   //<--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}

