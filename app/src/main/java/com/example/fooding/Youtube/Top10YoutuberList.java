package com.example.fooding.Youtube;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Top10YoutuberList {
    public String channelName = new String();
    public ArrayList<String> resNameList = new ArrayList<>();
    public ArrayList<Integer> indexList = new ArrayList<>();

    public Top10YoutuberList() {
        this.channelName = channelName;
        this.resNameList = resNameList;
    }

}
