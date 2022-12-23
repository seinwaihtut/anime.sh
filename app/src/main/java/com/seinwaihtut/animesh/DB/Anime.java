package com.seinwaihtut.animesh.DB;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anime_table")
public class Anime implements Parcelable {
    @PrimaryKey
    private Integer mal_id;

    private String mal_url;

    private String image_url;

    private String title; //Default title

    private String title_en; //English , Optional

    private String title_jp; //Japanese title, Optional

    private String type; //optional

    private String source; //optional

    private Integer episodes; //optional

    private String aired_string; //optional 	"Oct 12, 2022 to ?"

    private Float score = Float.valueOf(0); //optional

    private String synopsis; //optional

    private String season; //optional e.g. fall

    private Integer year; //optional e.g. 2022

    private String broadcast_string; //optional e.g. "Wednesdays at 00:00 (JST)"

    private String genres; //optional

    protected Anime(Parcel in) {
        if (in.readByte() == 0) {
            mal_id = null;
        } else {
            mal_id = in.readInt();
        }
        mal_url = in.readString();
        image_url = in.readString();
        title = in.readString();
        title_en = in.readString();
        title_jp = in.readString();
        type = in.readString();
        source = in.readString();
        if (in.readByte() == 0) {
            episodes = null;
        } else {
            episodes = in.readInt();
        }
        aired_string = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readFloat();
        }
        synopsis = in.readString();
        season = in.readString();
        if (in.readByte() == 0) {
            year = null;
        } else {
            year = in.readInt();
        }
        broadcast_string = in.readString();
        genres = in.readString();
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

    public Integer getMal_id() {
        return mal_id;
    }

    public void setMal_id(Integer mal_id) {
        this.mal_id = mal_id;
    }

    public String getMal_url() {
        return mal_url;
    }

    public void setMal_url(String mal_url) {
        this.mal_url = mal_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_jp() {
        return title_jp;
    }

    public void setTitle_jp(String title_jp) {
        this.title_jp = title_jp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public String getAired_string() {
        return aired_string;
    }

    public void setAired_string(String aired_string) {
        this.aired_string = aired_string;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBroadcast_string() {
        return broadcast_string;
    }

    public void setBroadcast_string(String broadcast_string) {
        this.broadcast_string = broadcast_string;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Anime(){
        //empty comstructor needed
    }

    public Anime(Integer mal_id, String mal_url, String image_url, String title) {
        this.mal_id = mal_id;
        this.mal_url = mal_url;
        this.image_url = image_url;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (mal_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mal_id);
        }
        dest.writeString(mal_url);
        dest.writeString(image_url);
        dest.writeString(title);
        dest.writeString(title_en);
        dest.writeString(title_jp);
        dest.writeString(type);
        dest.writeString(source);
        if (episodes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(episodes);
        }
        dest.writeString(aired_string);
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(score);
        }
        dest.writeString(synopsis);
        dest.writeString(season);
        if (year == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(year);
        }
        dest.writeString(broadcast_string);
        dest.writeString(genres);
    }
}