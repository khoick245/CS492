package com.nkdroid.tinderswipe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khoinguyen on 3/6/17.
 */
//@SuppressWarnings("serial")
public class Data implements Parcelable{

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

    public Data(Parcel in){
        String[] data = new String[10];
        in.readStringArray(data);

        this.id = data[0];
        this.name = data[1];
        this.categories = data[2];
        this.image_url = data[3];
        this.rating = data[4];
        this.phone = data[5];
        this.address = data[6];
        this.latitude = data[7];
        this.longitude = data[8];
        this.status = data[9];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.id,this.name,this.categories,this.image_url,this.rating,this.phone,this.address,this.latitude,this.longitude,this.status});
    }


    public static final Parcelable.Creator<Data> CREATOR= new Parcelable.Creator<Data>() {

        @Override
        public Data createFromParcel(Parcel source) {
// TODO Auto-generated method stub
            return new Data(source);  //using parcelable constructor
        }

        @Override
        public Data[] newArray(int size) {
// TODO Auto-generated method stub
            return new Data[size];
        }
    };
}
