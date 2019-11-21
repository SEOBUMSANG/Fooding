package com.example.fooding.WorldCup;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.fooding.Target.TargetList;

public class WorldcupTarget implements Parcelable {
    String name;

    public WorldcupTarget(String name) {
        this.name = name;
    }

    public WorldcupTarget(Parcel src) {
        name = src.readString();
    }

    public static final Creator CREATOR = new Creator() {
        public WorldcupTarget createFromParcel(Parcel src) {
            return new WorldcupTarget(src);
        }

        public WorldcupTarget[] newArray(int size) {
            return new WorldcupTarget[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }




}
