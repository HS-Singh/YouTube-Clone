package com.example.youtubeplayer;

import android.graphics.Bitmap;

public class VideoItem {
    private Bitmap thumbnail=null;
    private String title;
    private String img_url;
    private String videoId;

    VideoItem(String title, String img_url, String id)
    {
        this.title = title;
        this.img_url = img_url;
        videoId=id;
    }



    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
