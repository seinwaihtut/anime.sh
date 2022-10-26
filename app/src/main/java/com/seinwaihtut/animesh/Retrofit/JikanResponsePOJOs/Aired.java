
package com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Aired {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("prop")
    @Expose
    private Prop prop;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Aired() {
    }

    /**
     * 
     * @param prop
     * @param from
     * @param to
     */
    public Aired(String from, String to, Prop prop) {
        super();
        this.from = from;
        this.to = to;
        this.prop = prop;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Prop getProp() {
        return prop;
    }

    public void setProp(Prop prop) {
        this.prop = prop;
    }

}
