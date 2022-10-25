
package com.seinwaihtut.animesh.Airing.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Jikanv4SeasonNowResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jikanv4SeasonNowResponse() {
    }

    /**
     * 
     * @param pagination
     * @param data
     */
    public Jikanv4SeasonNowResponse(List<Datum> data, Pagination pagination) {
        super();
        this.data = data;
        this.pagination = pagination;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
