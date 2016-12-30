package org.openhpi.smarthome2016.lcd;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.wiringpi.Lcd;

/**
 * Created by pi on 27.12.16.
 * Controlling GE_C1602B display type with 2 rows, 16 columns and back light
 * (Gleichmann GE-C1602B-TMI-JT/R)
 * installed on the embedded smarthome board used with the openHPI course 2016.
 */
public class GE_C1602B {

    private static LCD_GE_C1602B instance;

    static{
        instance = new LCD_GE_C1602B();
    }

    /**
     * @return 2
     */
    public static int getRowCount() {
        return instance.getRowCount();
    }

    /**
     * @return 16
     */
    public static int getColumnCount() {
        return instance.getColumnCount();
    }

    /**
     * clears the display
     */
    public static void clear() {
        instance.clear();
    }

    /**
     * <p>
     * Set the cursor to the home position.
     * </p>
     *
     * @see <a href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     */
    public static void setCursorHome() {
        instance.setCursorHome();
    }

    /**
     * the the cursor at a given position
     * @param row
     * @param column
     */
    public static void setCursorPosition(int row, int column) {
        instance.setCursorPosition(row,column);
    }

    /**
     * <p>Write a single character of data to the LCD display. </p>
     * <p>(ATTENTION: the 'data' argument can only be a maximum of 512 characters.)</p>
     * @see <a href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param data
     */
    public static void write(byte data) {
        instance.write(data);
    }

    /**
     * <p>Write string of data to the LCD display.</p>
     * @see <a href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param data
     */
    public static void write(String data) {
        instance.write(data);
    }

    /**
     * <p>Write string of data to the LCD display row.</p>
     * @param row
     * @param data
     */
    public static void write(int row, String data) {
        instance.write(row, data);
    }

    /**
     * <p>Write string of data to the LCD display column at row.</p>
     * @param row
     * @param data
     */
    public static void write(int row, int column, String data) {
        instance.write(row, column, data);
    }

    /**
     * <p>Write string of data to the LCD display row at the given alignment position.
     * Filling the complete row.</p>
     * @param row
     * @param data
     */
    public static void writeln(int row, String data, LCDTextAlignment alignment) {
        instance.writeln(row, data, alignment);
    }

    /**
     * <p>Write string of data to the LCD display row at the given alignment position.</p>
     * @param row
     * @param data
     */
    public static void write(int row, String data, LCDTextAlignment alignment) {
        instance.writeln(row, data, alignment);
    }
    /**
     * <p>Write string of data to the LCD display row at left alignment position.</p>
     * @param row
     * @param data
     */
    public static void writeln(int row, String data) {
        instance.writeln(row, data);
    }

    /**
     * switch backlight on
     */
    public static void setBacklightOn(){
        instance.setBacklightOn();
    }

    /**
     * switch backlight off
     */
    public static void setBacklightOff(){
        instance.setBacklightOff();

    }
}

/**
 * LCD implementation for GE_C1602B display type with 2 rows, 16 columns and back light.
 *
 */
class LCD_GE_C1602B extends LCDBase implements LCD {

    private int rows = 2;
    private int columns = 16;

    //GPIO Pins
    private final Pin rsPin        = RaspiPin.GPIO_06;  // mapped to BMC 25;
    private final Pin strobePin        = RaspiPin.GPIO_04;  //23;
    private final  Pin lcd_d4        = RaspiPin.GPIO_25; //26;
    private final  Pin lcd_d5        = RaspiPin.GPIO_27; //16;
    private final  Pin lcd_d6        = RaspiPin.GPIO_28; //20;
    private final  Pin lcd_d7        = RaspiPin.GPIO_29; //21;

    // lcdHandle created in constructor
    private int lcdHandle;

    public LCD_GE_C1602B(){
        initialize(rows,columns, rsPin,  // LCD RS pin
                strobePin,  // LCD strobe pin
                lcd_d4,  // LCD data bit 1
                lcd_d5,  // LCD data bit 2
                lcd_d6,  // LCD data bit 3
                lcd_d7);
    }

    private void initialize(int rows, int columns, Pin rsPin, Pin strobePin, Pin... dataPins) {
        GpioUtil.enableNonPrivilegedAccess();

        this.rows = rows;
        this.columns = columns;
        int bits[] = { 0,0,0,0,0,0,0,0 };

        // set wiringPi interface for internal use
        // we will use the WiringPi pin number scheme with the wiringPi library
        //com.pi4j.wiringpi.Gpio.wiringPiSetup();

        // seed bit pin address array
        for(int index = 0; index < 8; index++) {
            if(index < dataPins.length)
                bits[index] = dataPins[index].getAddress();
        }

        // initialize LCD
        lcdHandle = Lcd.lcdInit(rows,
                columns,
                dataPins.length,
                rsPin.getAddress(),
                strobePin.getAddress(),
                bits[0], bits[1], bits[2], bits[3], bits[4], bits[5], bits[6], bits[7]);

        // verify LCD initialization
        if (lcdHandle == -1){
            throw new RuntimeException("Invalid LCD handle returned from wiringPi: " + lcdHandle);
        }
        // make a reservation on gpio pin 5 for LCD backlight
        GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_05);
        setBacklightOn();
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public void clear() {
        Lcd.lcdClear(lcdHandle);
    }

    @Override
    public void setCursorHome() {
        Lcd.lcdHome(lcdHandle);
    }

    @Override
    public void setCursorPosition(int row, int column) {
        validateCoordinates(row, column);
        Lcd.lcdPosition(lcdHandle, column, row);
    }

    @Override
    public void write(byte data) {
        Lcd.lcdPutchar(lcdHandle, data);
    }

    @Override
    public void write(String data) {
        Lcd.lcdPuts(lcdHandle, data);
    }

    public void setBacklightOn(){
        ((GpioPinDigitalOutput) GpioFactory.getInstance().getProvisionedPin(RaspiPin.GPIO_05)).high();

    }

    public void setBacklightOff(){
        ((GpioPinDigitalOutput) GpioFactory.getInstance().getProvisionedPin(RaspiPin.GPIO_05)).low();
    }

}