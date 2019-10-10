package com.example.fooding.Youtube;

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeItem implements Parcelable {
    public int id;
    public String link;
    public String thumbnail;
    public String title;
    public String youtuber;
    public int hits;
    public String date;

    public YoutubeItem(int id, String link, String thumbnail, String title, String youtuber, int hits, String date) {
        this.id = id;
        this.link = link;
        this.thumbnail = thumbnail;
        this.title = title;
        this.youtuber = youtuber;
        this.hits = hits;
        this.date = date;
    }

    public YoutubeItem(Parcel src) {
        id = src.readInt();
        link = src.readString();
        thumbnail = src.readString();
        title = src.readString();
        youtuber = src.readString();
        hits = src.readInt();
        date = src.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(link);
        dest.writeString(thumbnail);
        dest.writeString(title);
        dest.writeString(youtuber);
        dest.writeInt(hits);
        dest.writeString(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getYoutuber() {
        return youtuber;
    }

    public void setYoutuber(String youtuber) {
        this.youtuber = youtuber;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
