package com.nkdroid.tinderswipe;

/**
 * Created by khoinguyen on 3/6/17.
 */
public class Data {

    private  String id;
    private  String name;
    private  String categories;
    private  String image_url;
    private  String rating;
    private  String phone;
    private  String address;
    private  String latitude;
    private  String longitude;
    private  String status;

    public Data(){}

    public Data(String id, String name, String categories, String image_url, String rating, String phone, String address, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.image_url = image_url;
        this.rating = rating;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getCategories() {
        return categories;
    }

    public String getName() {

        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getRating() {
        return rating;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
