package org.openhpi.smarthome2016.spi;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import org.openhpi.smarthome2016.examples.DisplayTemperature;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

/**
 * Created by pi on 25.12.16.
 * Data access methods for Lm335Z temperature sensor, HMZ-333A1 Humidity module and MCP3008 ADC Module
 * installed on the embedded smarthome board used with the openHPI course 2016.<br>
 * Additionally the reference voltage multiplicand can be set to support some kind of calibration.
 * @see DisplayTemperature
 *
 */
public class MCP3008 {

    private MCP3008(){}

    private static SpiDevice mcp3008;

    // reference voltage defaults to 3.3V
    private static double vRef = 3.3;

    static{
        try {
            mcp3008 = SpiFactory.getInstance(SpiChannel.CS0,
                    SpiDevice.DEFAULT_SPI_SPEED/2, // default spi speed 1 MHz : raspberryPi speed = 500kHz
                    SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
            vRef = loadReferenceVoltage();
        } catch (IOException e) {
            mcp3008  = null;
            e.printStackTrace();
        }
    }

    /**
     * get device instance for writing;
     * @return the mcp3008 instance
     * @throws IOException
     */
    public static SpiDevice getDevice() throws IOException {
        if (null == mcp3008) {
            throw new NullPointerException("MCP3008 ADC Module not initialized");
        }
        return mcp3008;
    }

    /**
     * Communicate to the ADC chip via SPI to get single-ended spi value for a specified channel.
     * @param channel analog input channel on ADC chip
     * @return spi value for specified analog input channel
     * @throws IOException
     */
    public static int getConversionValue(short channel) throws IOException {

        // create a data buffer and initialize a spi request payload
        byte data[] = new byte[] {
                (byte) 0b00000001,                              // first byte, start bit
                (byte)(0b10000000 |( ((channel & 7) << 4))),    // second byte transmitted -> (SGL/DIF = 1, D2=D1=D0=0)
                (byte) 0b00000000                               // third byte transmitted....don't care
        };

        // send spi request to ADC chip via SPI channel
        byte[] result = getDevice().write(data);

        // calculate and return spi value from result bytes
        int value = (result[1]<< 8) & 0b1100000000; //merge data[1] & data[2] to get 10-bit result (0-1023 as int)
        value |=  (result[2] & 0xff);
        return value;
    }

    /**
     * convenience method to get the temperature in Celsius from indoor sensor
     * @return temperature in celsius as double with 4 digits
     */
    public static double getIndoorTemperature() throws IOException {
        short channel = 1;
        int places = 4;
        return voltsToCesius(convertToVolts(getConversionValue(channel),places),places);
    }

    /**
     * convenience method to get the temperature in Celsius from indoor sensor
     * @return temperature in celsius as double with 4 digits
     */
    public static double getOutdoorTemperature() throws IOException {
        short channel = 2;
        int places = 4;
        return voltsToCesius(convertToVolts(getConversionValue(channel),places),places);
    }

    public static double getRelativeHumidity() throws IOException {
        short channel = 0;
        int places = 4;
        double volts = convertToVolts(getConversionValue(channel),places);
        return voltsToHumidity(volts,getIndoorTemperature(),places);
    }

    /**
     * used to calibrate ADC should be the exact value messured at board
     * defaults to 3.3
     * @param referenceVoltage from 1 to 3.3
     */
    public static void setReferenceVoltage(double referenceVoltage){
        if (referenceVoltage < 1 || referenceVoltage > 3.3){
            throw new IllegalArgumentException("reference voltage must be between 1 and 3.3 volts");
        }
        vRef = referenceVoltage;
        storeReferenceVoltage(referenceVoltage);

    }

    public static double getReferenceVoltage() {
        return vRef;
    }



    static double voltsToHumidity(double volts, double temp, int places){
        double humidity = 0.0;
        if (temp <= 22.5){
            humidity = round((volts - 0.128) / 0.0286, places);
        } else if ( temp <= 27.5) {
            humidity = round((volts - 0.085) / 0.0294, places);
        } else {
            humidity = round((volts - 0.038) / 0.0296, places);
        }
        return humidity;
    }

    static double convertToVolts(int data, int places){
        // linear spi 0 - 3,3V  <- mostly a calibration is required 3,26 in my case
        double volts = (data * vRef) / 1023D;
        volts = round(volts,places);
        return volts;
    }

    static double voltsToCesius(double volts, int places){
        // 10mV = 1 degree kelvin
        return round((volts * 100) - 273.15, places);
    }

    static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    static double loadReferenceVoltage(){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("mcp3008.properties"));
            if (prop.containsKey("vRef")){
               return new Double(prop.getProperty("vRef")).doubleValue();
            }
        } catch (IOException e) {
           // silently ignore exception and return default value;
        }
        return vRef;
    }

    static void storeReferenceVoltage(double vRef)  {
        // make vRef persistent
        Properties prop = new Properties();
        prop.setProperty("vRef", String.valueOf(vRef));
        try {
             prop.store(new FileOutputStream("mcp3008.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
