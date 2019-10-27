package com.example.fooding.Youtube;

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeItem implements Parcelable {
    public String thumbnail;
    public String channel;
    public String description;
    public String title;
    public String URL;

    public YoutubeItem(String thumbnail, String channel, String description, String title, String URL) {
        this.thumbnail = thumbnail;
        this.channel = channel;
        this.description = description;
        this.title = title;
        this.URL = URL;
    }

    public YoutubeItem(Parcel src) {
        thumbnail = src.readString();
        channel = src.readString();
        description = src.readString();
        title = src.readString();
        URL = src.readString();
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
        dest.writeString(thumbnail);
        dest.writeString(channel);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(URL);
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
