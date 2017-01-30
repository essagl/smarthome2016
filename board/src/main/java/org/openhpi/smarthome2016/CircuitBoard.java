package org.openhpi.smarthome2016;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.openhpi.smarthome2016.lcd.GE_C1602B.clear;
import static org.openhpi.smarthome2016.lcd.GE_C1602B.setBacklightOff;

/**
 * Created by ulrich on 15.01.17.
 * This class provides access to the circuit board elements like buttons leds display and sensor values
 * and activates features like thermometer display, room climate surveillance.
 * PI4J library is not used as a dependency.
 */
public class CircuitBoard {
    public static final double MINIMUM_DIFFERENCE = 0.3;
    private static boolean running = false;
    private static Thread boardThread;
    private static boolean displaying = true;
    private final static int LCD_ROW_1 = 0;
    private final static int LCD_ROW_2 = 1;
    private String roomClimate = "unknown";
    private boolean roomClimateOK = true;
    private ServiceInterface boardService;



    /**
     * instantiate the CircuitBoard with a board service
     * @param boardService
     */
    public CircuitBoard(ServiceInterface boardService) {
        this.boardService = boardService;
        this.boardThread = new Thread(runThread);
        this.boardThread.start();
    }


    /**
     * suppress/enable display messages for main thread
     * @param displaying
     */
    public void setDisplaying(boolean displaying){
        this.displaying = displaying;
    }

    /**
     * access to the board service used
     * @return the board service
     */
    public ServiceInterface getBoardService(){
        return boardService;
    }

    public String getRoomClimateInfo(){
        return roomClimate;
    }

    public boolean isRoomClimateOK(){
        return roomClimateOK;
    }

    /**
     * shutdown the main task
     */
    public void shutdown(){
        running = false;
    }


    private Runnable runThread = () -> {
        running = true;
        boardService.setupGPIO();
        double lastIndoorTemp =  0;
        double lastOutdoorTemp =  0;
        double lastHumidity = 0;
       // char celsius = (char)223;
        clearDisplay();
        boolean valueChanged = true;
        while(running) {

            try {
                double indoorTemp =  boardService.getIndoorTemp();
                double outdoorTemp =  boardService.getOutdoorTemp();
                double humidity = boardService.getHumidity();
                if (indoorTemp < 16) {
                    roomClimate = "Raum zu kalt";
                    roomClimateOK = false;
                } else if(indoorTemp > 28) {
                    boardService.setGreenLedOff();
                    boardService.setRedLedOn();
                    roomClimate = "Raum zu warm";
                    roomClimateOK = false;
                } else if(outdoorTemp < 16 && indoorTemp < 16 && boardService.isSwitch1Open()) {
                     roomClimate = "Fenster zumachen";
                    roomClimateOK = false;
                } else if (isRelHumidityOk(outdoorTemp,indoorTemp,humidity)){
                    roomClimate = "Raumklima OK";
                    roomClimateOK = true;
                } else  {
                    roomClimate = "Bitte lueften";
                    roomClimateOK = false;
                }
                if (valueChanged) {
                    if (roomClimateOK){
                        boardService.setGreenLedOn();
                        boardService.setRedLedOFF();
                    } else {
                        boardService.setGreenLedOff();
                        boardService.setRedLedOn();

                    }
                    message(roomClimate,LCD_ROW_1,displaying);
                    String row2 = String.format("%1$.1f %2$.1f %3$.1f",lastOutdoorTemp,lastIndoorTemp,lastHumidity );
                    if (row2.length() > 16){
                        row2 = row2.substring(0,15);
                    }
                    message(row2,LCD_ROW_2,displaying);
                    valueChanged = false;
                }

                if ((indoorTemp > lastIndoorTemp + MINIMUM_DIFFERENCE) || (indoorTemp < lastIndoorTemp - MINIMUM_DIFFERENCE)){
                    lastIndoorTemp =  indoorTemp;
                    valueChanged = true;
                }
                if ((outdoorTemp > lastOutdoorTemp + MINIMUM_DIFFERENCE) || (outdoorTemp < lastOutdoorTemp - MINIMUM_DIFFERENCE)){
                    lastOutdoorTemp =  outdoorTemp;
                    valueChanged = true;
                }
                if ((humidity > lastHumidity + MINIMUM_DIFFERENCE) || (humidity < lastHumidity - MINIMUM_DIFFERENCE)){
                    lastHumidity = humidity;
                    valueChanged = true;
                }

                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }


        }
        exit();
    };


    private void message(String message,int row, boolean really){
        if (really) {
            boardService.setMessage(message,row);
        }
    }



    private  void exit() {
        // shutdown all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)

        try {
            clearDisplay();
            boardService.setLcdBackLightOFF();
            Thread.sleep(100);
            boardService.setGreenLedOff();
            boardService.setRedLedOFF();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boardService.shutdownGPIO();

    }



    private void clearDisplay() {
        // clear LCD
        boardService.clearDisplay();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            running = false;
        }
    }

    private  boolean isRelHumidityOk(double temp_out,double temp_in, double humidity ){

        if (humidity < 25 || humidity > 80){
            return false;
        }
        if (temp_out <= 5){
           if (temp_in <= 18 && humidity <= 60){
               return true;
           } else if (temp_in > 18 && temp_in <= 21 && humidity <= 50){
               return true;
           } else if (temp_in > 21 && humidity <= 40){
               return true;
           } else {
               return false;
           }
        }  else if (temp_out > 5 && temp_out <= 15){
           if (temp_in <= 18 && humidity <= 70){
               return true;
           } else if (temp_in > 18 && temp_in <= 21 && humidity <= 60){
               return true;
           } else if (temp_in > 21 && humidity <= 50){
               return true;
           } else {
               return false;
           }
        } else {
           return true;
        }

    }
}

