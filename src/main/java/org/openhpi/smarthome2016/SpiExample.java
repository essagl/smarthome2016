package org.openhpi.smarthome2016;

/**
 * Created by pi on 24.12.16.
 */

import com.pi4j.util.Console;
import org.openhpi.smarthome2016.spi.MCP3008;

import java.io.IOException;

/**
 * This example code demonstrates how to perform basic SPI communications using the Raspberry Pi.
 * CS0 and CS1 (ship-select) are supported for SPI0.
 *
 * @author Robert Savage
 */
public class SpiExample {

    // SPI device
    //public static SpiDevice spi = null;

    // ADC channel count
    //public static short ADC_CHANNEL_COUNT = 8;  // MCP3004=4, MCP3008=8

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    protected static final Console console = new Console();

    /**
     * Sample SPI Program
     *
     * @param args (none)
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String args[]) throws InterruptedException, IOException {

        // print program title/header
        console.title("<-- OpenHPI Smarthome  -->", "SPI test program using MCP3008 AtoD Chip");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // This SPI example is using the Pi4J SPI interface to communicate with
        // the SPI hardware interface connected to a MCP3004/MCP3008 AtoD Chip.
        //
        // Please make sure the SPI is enabled on your Raspberry Pi via the
        // raspi-config utility under the advanced menu option.
        //
        // see this blog post for additional details on SPI and WiringPi
        // http://wiringpi.com/reference/spi-library/
        //
        // see the link below for the data sheet on the MCP3004/MCP3008 chip:
        // http://ww1.microchip.com/downloads/en/DeviceDoc/21294E.pdf



        // continue running program until user exits using CTRL-C
        while(console.isRunning()) {
            double indoorTemp = MCP3008.getIndoorTemperature();
            double outdoorTemp = MCP3008.getOutdoorTemperature();
            double humidity = MCP3008.getRelativeHumidity();
            console.print("Indoor temp: "+indoorTemp+" Outdoor temp:"+outdoorTemp+" Relative humidity:"+humidity+"%");

            Thread.sleep(1000);
            console.print(" |\r");
        }
        console.emptyLine();
    }


}
