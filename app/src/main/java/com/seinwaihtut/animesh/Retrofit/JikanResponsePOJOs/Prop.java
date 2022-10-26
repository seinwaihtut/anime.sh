
package com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Prop {

    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("to")
    @Expose
    private To to;
    @SerializedName("string")
    @Expose
    private String string;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Prop() {
    }

    /**
     * 
     * @param string
     * @param from
     * @param to
     */
    public Prop(From from, To to, String string) {
        super();
        this.from = from;
        this.to = to;
        this.string = string;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}
