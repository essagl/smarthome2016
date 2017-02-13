package org.openhpi.smarthome2016;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by ulrich on 12.02.17.
 */
public class CircuitBoardTest {

    @Test
    public void RelHumidityTest(){
        CircuitBoard board = new CircuitBoard(new ServiceMockImpl());
        double tempIn = 20.0;
        double tempOut = 4.0;
        double humidity = 50.0;
        Assertions.assertTrue( board.isRelHumidityOk(tempOut,tempIn,humidity));

        tempIn = 10.0;
        tempOut = 4.0;
        humidity = 50.0;
        Assertions.assertTrue( board.isRelHumidityOk(tempOut,tempIn,humidity));

        tempIn = 20.0;
        tempOut = 4.0;
        humidity = 50.0;
        Assertions.assertTrue( board.isRelHumidityOk(tempOut,tempIn,humidity));

        tempIn = 25.0;
        tempOut = 4.0;
        humidity = 65.0;
        Assertions.assertFalse( board.isRelHumidityOk(tempOut,tempIn,humidity));



    }


    @Test
    public void RoomClimateTest(){
        CircuitBoard board = new CircuitBoard(new ServiceMockImpl());
        double tempIn = 20.0;
        double tempOut = 4.0;
        double humidity = 50.0;
        board.calculateRoomClimate(tempOut,tempIn,humidity);
        Assertions.assertTrue( board.isRoomClimateOK());

        tempIn = 10.0;
        tempOut = 4.0;
        humidity = 50.0;
        board.calculateRoomClimate(tempOut,tempIn,humidity);
        Assertions.assertFalse( board.isRoomClimateOK());

        // etc...
    }

}
