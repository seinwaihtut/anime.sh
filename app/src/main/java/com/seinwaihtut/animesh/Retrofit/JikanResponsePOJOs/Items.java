
package com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Items {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Items() {
    }

    /**
     * 
     * @param total
     * @param perPage
     * @param count
     */
    public Items(Integer count, Integer total, Integer perPage) {
        super();
        this.count = count;
        this.total = total;
        this.perPage = perPage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

}
