package org.openhapi.smarthome2016.server.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by ulrich on 07.01.17.
 */
@Entity
@Table(name = "data")
@NamedQueries(
        {
                @NamedQuery(
                        name = "org.openhapi.smarthome2016.server.core.BoardData.findNewerThan",
                        query = "SELECT d FROM BoardData d where timestamp > :instant"
                )
        }
)
@ApiModel
public class BoardData  {

    @Id
    @ApiModelProperty(position = 1, value = "Unique id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ApiModelProperty(position = 2,  value = "Date time info")
    @Column(name = "timestamp", nullable = false)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private DateTime timestamp;


    @Column(name = "outdoorTemp", nullable = false)

    @ApiModelProperty(position = 3, required = true, value = "Outdoor temperature as double value")
    private  double outdoorTemp;

    @Column(name = "indoorTemp", nullable = false)

    @ApiModelProperty(position = 4, required = true, value = "Indoor temperature as double value")
    private  double indoorTemp;

    @Column(name = "humidity", nullable = false)

    @ApiModelProperty(position = 5, required = true, value = "Indoor humidity as double value")
    private  double humidity;

    @ApiModelProperty(position = 6,  value = "State of switch true = closed")
    @Column(name = "switchClosed", nullable = false)
    private boolean switchClosed;

    @ApiModelProperty(position = 5,  value = "Message")
    @Column(name = "message", nullable = false)
    private String message;

    public BoardData() {
    }



    public BoardData(DateTime timestamp,
                     double outdootTemp,
                     double indoorTemup,
                     double humidity,
                     boolean switchClosed,
                     String messsge) {
        this.timestamp = timestamp;
        this.outdoorTemp = outdootTemp;
        this.indoorTemp = indoorTemup;
        this.humidity = humidity;
        this.switchClosed =switchClosed;
        this.message = messsge;
    }

    public long getId() {
        return id;
    }


    public double getOutdoorTemp() {
        return outdoorTemp;
    }

    public void setOutdoorTemp(double outdoorTemp) {
        this.outdoorTemp = outdoorTemp;
    }

    public double getIndoorTemp() {
        return indoorTemp;
    }

    public void setIndoorTemp(double indoorTemp) {
        this.indoorTemp = indoorTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public boolean isSwitchClosed() {
        return switchClosed;
    }

    public void setSwitchClosed(boolean switchClosed) {
        this.switchClosed = switchClosed;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardData boardData = (BoardData) o;

        if (getId() != boardData.getId()) return false;
        if (Double.compare(boardData.getOutdoorTemp(), getOutdoorTemp()) != 0) return false;
        if (Double.compare(boardData.getIndoorTemp(), getIndoorTemp()) != 0) return false;
        if (Double.compare(boardData.getHumidity(), getHumidity()) != 0) return false;
        if (isSwitchClosed() != boardData.isSwitchClosed()) return false;
        if (timestamp != null ? !timestamp.equals(boardData.timestamp) : boardData.timestamp != null) return false;
        return getMessage() != null ? getMessage().equals(boardData.getMessage()) : boardData.getMessage() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        temp = Double.doubleToLongBits(getOutdoorTemp());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getIndoorTemp());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getHumidity());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isSwitchClosed() ? 1 : 0);
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", datetime='" + timestamp + '\'' +
                ", indoorTemp='" + indoorTemp + '\'' +
                ", outdoorTemp='" + outdoorTemp + '\'' +
                ", humidity='" + humidity + '\'' +
                ", switchClosed='" + switchClosed + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}


