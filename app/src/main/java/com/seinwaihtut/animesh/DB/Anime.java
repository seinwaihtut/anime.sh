package com.seinwaihtut.animesh.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anime_table")
public class Anime {
    @NonNull
    @PrimaryKey()
    private String mal_id;

    @ColumnInfo(name = "mal_url")
    private String mal_url;

    @ColumnInfo(name = "image_url")
    private String image_url;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "score")
    private String score;

    @ColumnInfo(name = "no_episodes")
    private String no_episodes;

    @ColumnInfo(name = "genres")
    private String genres;

    @ColumnInfo(name = "synopsis")
    private String synopsis;

    @ColumnInfo(name = "airing_start")
    private String airing_start;

    @ColumnInfo(name = "broadcast")
    private String broadcast;


    public Anime() {
    }

    public Anime(String mal_id, String mal_url, String image_url, String title, String score, String no_episodes, String genres, String synopsis, String airing_start, String broadcast) {
        this.mal_id = mal_id;
        this.mal_url = mal_url;
        this.image_url = image_url;
        this.title = title;
        this.score = score;
        this.no_episodes = no_episodes;
        this.genres = genres;
        this.synopsis = synopsis;
        this.airing_start = airing_start;
        this.broadcast = broadcast;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public String getMal_url() {
        return this.mal_url;
    }

    public String getScore() {
        return this.score;
    }

    public String getAiring_start() {
        return this.airing_start;
    }

    public String getNo_episodes() {
        return this.no_episodes;
    }

    public String getGenres() {
        return this.genres;
    }

    public String getBroadcast() {
        return this.broadcast;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public String getMal_id() {
        return mal_id;
    }

    public void setMal_id(String mal_id) {
        this.mal_id = mal_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_url(String imageUrl) {
        this.image_url = imageUrl;
    }

    public void setMal_url(String mal_url) {
        this.mal_url = mal_url;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setAiring_start(String airing_start) {
        this.airing_start = airing_start;
    }

    public void setNo_episodes(String no_episodes) {
        this.no_episodes = no_episodes;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
