package com.nkdroid.tinderswipe;

/**
 * Created by khoinguyen on 3/6/17.
 */

public class Restaurant {
    private String id;
    private String image_Url;
    private String status;          // like/dislike
    private String date_do_status;  // date time the restaurant is liked/disliked

    public Restaurant(String id, String image_Url, String status, String date_do_status) {
        this.id = id;
        this.image_Url = image_Url;
        this.status = status;
        this.date_do_status = date_do_status;
    }

    public String getId() {
        return id;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public String getStatus() {
        return status;
    }

    public String getDate_do_status() {
        return date_do_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate_do_status(String date_do_status) {
        this.date_do_status = date_do_status;
    }
}
