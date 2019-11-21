package com.example.fooding.Youtube;

import android.os.Parcel;
import android.os.Parcelable;

public class ResImageUrl implements Parcelable {
    public String resName;
    public String resImageUrl1;
    public String resImageUrl2;

    public ResImageUrl(String resName, String resImageUrl1, String resImageUrl2){
        this.resName = resName;
        this.resImageUrl1 = resImageUrl1;
        this.resImageUrl2 = resImageUrl2;
    }

    protected ResImageUrl(Parcel in) {
        resImageUrl1 = in.readString();
        resImageUrl2 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resImageUrl1);
        dest.writeString(resImageUrl2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResImageUrl> CREATOR = new Creator<ResImageUrl>() {
        @Override
        public ResImageUrl createFromParcel(Parcel in) {
            return new ResImageUrl(in);
        }

        @Override
        public ResImageUrl[] newArray(int size) {
            return new ResImageUrl[size];
        }
    };
}
