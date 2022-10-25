
package com.seinwaihtut.animesh.Airing.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Datum {

    @SerializedName("mal_id")
    @Expose
    private Integer malId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("trailer")
    @Expose
    private Trailer trailer;
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("titles")
    @Expose
    private List<Title> titles = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_english")
    @Expose
    private String titleEnglish;
    @SerializedName("title_japanese")
    @Expose
    private String titleJapanese;
    @SerializedName("title_synonyms")
    @Expose
    private List<String> titleSynonyms = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("episodes")
    @Expose
    private Integer episodes;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("airing")
    @Expose
    private Boolean airing;
    @SerializedName("aired")
    @Expose
    private Aired aired;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("score")
    @Expose
    private Float score;
    @SerializedName("scored_by")
    @Expose
    private Integer scoredBy;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;
    @SerializedName("members")
    @Expose
    private Integer members;
    @SerializedName("favorites")
    @Expose
    private Integer favorites;
    @SerializedName("synopsis")
    @Expose
    private String synopsis;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("season")
    @Expose
    private String season;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("broadcast")
    @Expose
    private Broadcast broadcast;
    @SerializedName("producers")
    @Expose
    private List<Producer> producers = null;
    @SerializedName("licensors")
    @Expose
    private List<Licensor> licensors = null;
    @SerializedName("studios")
    @Expose
    private List<Studio> studios = null;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("explicit_genres")
    @Expose
    private List<ExplicitGenre> explicitGenres = null;
    @SerializedName("themes")
    @Expose
    private List<Theme> themes = null;
    @SerializedName("demographics")
    @Expose
    private List<Demographic> demographics = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum() {
    }

    /**
     * 
     * @param favorites
     * @param broadcast
     * @param year
     * @param scoredBy
     * @param rating
     * @param source
     * @param title
     * @param type
     * @param trailer
     * @param duration
     * @param score
     * @param themes
     * @param approved
     * @param titleEnglish
     * @param genres
     * @param popularity
     * @param members
     * @param rank
     * @param season
     * @param airing
     * @param episodes
     * @param titleSynonyms
     * @param aired
     * @param images
     * @param studios
     * @param titles
     * @param synopsis
     * @param explicitGenres
     * @param licensors
     * @param url
     * @param producers
     * @param background
     * @param titleJapanese
     * @param malId
     * @param status
     * @param demographics
     */
    public Datum(Integer malId, String url, Images images, Trailer trailer, Boolean approved, List<Title> titles, String title, String titleEnglish, String titleJapanese, List<String> titleSynonyms, String type, String source, Integer episodes, String status, Boolean airing, Aired aired, String duration, String rating, Float score, Integer scoredBy, Integer rank, Integer popularity, Integer members, Integer favorites, String synopsis, String background, String season, Integer year, Broadcast broadcast, List<Producer> producers, List<Licensor> licensors, List<Studio> studios, List<Genre> genres, List<ExplicitGenre> explicitGenres, List<Theme> themes, List<Demographic> demographics) {
        super();
        this.malId = malId;
        this.url = url;
        this.images = images;
        this.trailer = trailer;
        this.approved = approved;
        this.titles = titles;
        this.title = title;
        this.titleEnglish = titleEnglish;
        this.titleJapanese = titleJapanese;
        this.titleSynonyms = titleSynonyms;
        this.type = type;
        this.source = source;
        this.episodes = episodes;
        this.status = status;
        this.airing = airing;
        this.aired = aired;
        this.duration = duration;
        this.rating = rating;
        this.score = score;
        this.scoredBy = scoredBy;
        this.rank = rank;
        this.popularity = popularity;
        this.members = members;
        this.favorites = favorites;
        this.synopsis = synopsis;
        this.background = background;
        this.season = season;
        this.year = year;
        this.broadcast = broadcast;
        this.producers = producers;
        this.licensors = licensors;
        this.studios = studios;
        this.genres = genres;
        this.explicitGenres = explicitGenres;
        this.themes = themes;
        this.demographics = demographics;
    }

    public Integer getMalId() {
        return malId;
    }

    public void setMalId(Integer malId) {
        this.malId = malId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
    }

    public List<String> getTitleSynonyms() {
        return titleSynonyms;
    }

    public void setTitleSynonyms(List<String> titleSynonyms) {
        this.titleSynonyms = titleSynonyms;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAiring() {
        return airing;
    }

    public void setAiring(Boolean airing) {
        this.airing = airing;
    }

    public Aired getAired() {
        return aired;
    }

    public void setAired(Aired aired) {
        this.aired = aired;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getScoredBy() {
        return scoredBy;
    }

    public void setScoredBy(Integer scoredBy) {
        this.scoredBy = scoredBy;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public Broadcast getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void setProducers(List<Producer> producers) {
        this.producers = producers;
    }

    public List<Licensor> getLicensors() {
        return licensors;
    }

    public void setLicensors(List<Licensor> licensors) {
        this.licensors = licensors;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public void setStudios(List<Studio> studios) {
        this.studios = studios;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<ExplicitGenre> getExplicitGenres() {
        return explicitGenres;
    }

    public void setExplicitGenres(List<ExplicitGenre> explicitGenres) {
        this.explicitGenres = explicitGenres;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public List<Demographic> getDemographics() {
        return demographics;
    }

    public void setDemographics(List<Demographic> demographics) {
        this.demographics = demographics;
    }

}
