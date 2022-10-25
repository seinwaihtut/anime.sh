
package com.seinwaihtut.animesh.Airing.POJO;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Images {

    @SerializedName("jpg")
    @Expose
    private Jpg jpg;
    @SerializedName("webp")
    @Expose
    private Webp webp;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Images() {
    }

    /**
     * 
     * @param jpg
     * @param webp
     */
    public Images(Jpg jpg, Webp webp) {
        super();
        this.jpg = jpg;
        this.webp = webp;
    }

    public Jpg getJpg() {
        return jpg;
    }

    public void setJpg(Jpg jpg) {
        this.jpg = jpg;
    }

    public Webp getWebp() {
        return webp;
    }

    public void setWebp(Webp webp) {
        this.webp = webp;
    }

}
