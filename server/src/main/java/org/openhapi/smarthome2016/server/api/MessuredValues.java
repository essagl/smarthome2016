package org.openhapi.smarthome2016.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.Length;

import java.util.concurrent.atomic.AtomicLong;

public class MessuredValues {


    @Length(max = 5)
    private double indoorTemp;

    @Length(max = 5)
    private double outdoorTemp;

    @Length(max = 5)
    private double humidity;

    public MessuredValues(){
        // Jackson deserialization
    }


    public MessuredValues(long counter, double indoorTemp, double outdoorTemp, double humidity) {

        this.indoorTemp = indoorTemp;
        this.outdoorTemp = outdoorTemp;
        this.humidity = humidity;
    }




    @JsonProperty
    public double getIndoorTemp() {
        return indoorTemp;
    }

    @JsonProperty
    public double getOutdoorTemp() {
        return outdoorTemp;
    }

    @JsonProperty
    public double getHumidity() {
        return humidity;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("indoorTemp", indoorTemp)
                .add("outdoorTemp", outdoorTemp)
                .add("humidity", humidity)
                .toString();
    }
}
