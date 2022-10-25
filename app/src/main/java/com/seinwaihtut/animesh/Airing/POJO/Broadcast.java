
package com.seinwaihtut.animesh.Airing.POJO;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Broadcast {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("string")
    @Expose
    private String string;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Broadcast() {
    }

    /**
     * 
     * @param string
     * @param timezone
     * @param time
     * @param day
     */
    public Broadcast(String day, String time, String timezone, String string) {
        super();
        this.day = day;
        this.time = time;
        this.timezone = timezone;
        this.string = string;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}
