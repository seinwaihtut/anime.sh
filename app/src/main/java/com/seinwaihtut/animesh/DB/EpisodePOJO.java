package com.seinwaihtut.animesh.DB;

public class EpisodePOJO {
    private String nyaa_url;
    private String upload_title;
    private String torrent_url;
    private String magnet_url;
    private String upload_time;
    private String size;
    private Integer seeders;
    private Integer leechers;
    private Integer completed_downloads;

    public EpisodePOJO(String upload_title, String magnet_url) {
        this.upload_title = upload_title;
        this.magnet_url = magnet_url;
    }

    public String getNyaa_url() {
        return nyaa_url;
    }

    public void setNyaa_url(String nyaa_url) {
        this.nyaa_url = nyaa_url;
    }

    public String getUpload_title() {
        return upload_title;
    }

    public void setUpload_title(String upload_title) {
        this.upload_title = upload_title;
    }

    public String getTorrent_url() {
        return torrent_url;
    }

    public void setTorrent_url(String torrent_url) {
        this.torrent_url = torrent_url;
    }

    public String getMagnet_url() {
        return magnet_url;
    }

    public void setMagnet_url(String magnet_url) {
        this.magnet_url = magnet_url;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getSeeders() {
        return seeders;
    }

    public void setSeeders(Integer seeders) {
        this.seeders = seeders;
    }

    public Integer getLeechers() {
        return leechers;
    }

    public void setLeechers(Integer leechers) {
        this.leechers = leechers;
    }

    public Integer getCompleted_downloads() {
        return completed_downloads;
    }

    public void setCompleted_downloads(Integer completed_downloads) {
        this.completed_downloads = completed_downloads;
    }
}
