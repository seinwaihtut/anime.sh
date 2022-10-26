
package com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Trailer {

    @SerializedName("youtube_id")
    @Expose
    private String youtubeId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("embed_url")
    @Expose
    private String embedUrl;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Trailer() {
    }

    /**
     * 
     * @param embedUrl
     * @param youtubeId
     * @param url
     */
    public Trailer(String youtubeId, String url, String embedUrl) {
        super();
        this.youtubeId = youtubeId;
        this.url = url;
        this.embedUrl = embedUrl;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

}
