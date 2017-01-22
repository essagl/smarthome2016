package org.openhapi.smarthome2016.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.Length;

/**
 * Created by ulrich on 15.01.17.
 */
public class RoomClimate {
    @Length(max = 256)
    private String roomClimateInfo;


    private boolean roomClimateOK;



    public RoomClimate(){
        // Jackson deserialization
    }


    public RoomClimate( String roomClimateInfo, boolean roomClimateOK) {
        this.roomClimateInfo = roomClimateInfo;
        this.roomClimateOK = roomClimateOK;

    }




    @JsonProperty
    public boolean getRoomClimateOK() {
        return roomClimateOK;
    }

    @JsonProperty
    public String getRoomClimateInfo() {
        return roomClimateInfo;
    }



    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("roomClimateInfo", roomClimateInfo)
                .add("roomClimateOK", roomClimateOK)
                .toString();
    }
}