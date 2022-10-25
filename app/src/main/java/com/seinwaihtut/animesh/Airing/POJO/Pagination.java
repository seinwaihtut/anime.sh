
package com.seinwaihtut.animesh.Airing.POJO;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Pagination {

    @SerializedName("last_visible_page")
    @Expose
    private Integer lastVisiblePage;
    @SerializedName("has_next_page")
    @Expose
    private Boolean hasNextPage;
    @SerializedName("items")
    @Expose
    private Items items;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pagination() {
    }

    /**
     * 
     * @param hasNextPage
     * @param items
     * @param lastVisiblePage
     */
    public Pagination(Integer lastVisiblePage, Boolean hasNextPage, Items items) {
        super();
        this.lastVisiblePage = lastVisiblePage;
        this.hasNextPage = hasNextPage;
        this.items = items;
    }

    public Integer getLastVisiblePage() {
        return lastVisiblePage;
    }

    public void setLastVisiblePage(Integer lastVisiblePage) {
        this.lastVisiblePage = lastVisiblePage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

}
