package com.example.fooding.Target;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.fooding.Youtube.YoutubeItem;

import java.util.ArrayList;


public class TargetList implements Parcelable{
    public String name;
    public String lat;
    public String lng;
    public String description;
    public String resAddress;
    public String resImageURL;
    public String youtube;
    public ArrayList<YoutubeItem> youtubeItems;

    public TargetList(String name, String lat, String lng, String description, String resAddress, String resImageURL, String youtube, ArrayList<YoutubeItem> youtubeItems) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.resAddress = resAddress;
        this.resImageURL = resImageURL;
        this.youtube = youtube;
        this.youtubeItems = youtubeItems;
    }

    public TargetList(Parcel src) {
        name = src.readString();
        lat = src.readString();
        lng = src.readString();
        description = src.readString();
        resAddress = src.readString();
        resImageURL = src.readString();
        youtube = src.readString();
        youtubeItems = src.readArrayList(null);
    }

    public static final Creator CREATOR = new Creator() {
        public YoutubeItem createFromParcel(Parcel src) {
            return new YoutubeItem(src);
        }

        public YoutubeItem[] newArray(int size) {
            return new YoutubeItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { ;
        dest.writeString(name);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(description);
        dest.writeString(resAddress);
        dest.writeString(resImageURL);
        dest.writeString(youtube);
        dest.writeList(youtubeItems);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResImageURL() {
        return resImageURL;
    }

    public void setResImageURL(String resImageURL) {
        this.resImageURL = resImageURL;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public ArrayList<YoutubeItem> getYoutubeItems() {
        return youtubeItems;
    }

    public void setYoutubeItems(ArrayList<YoutubeItem> youtubeItems) {
        this.youtubeItems = youtubeItems;
    }
}
