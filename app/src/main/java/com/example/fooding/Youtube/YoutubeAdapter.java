package com.example.fooding.Youtube;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class YoutubeAdapter extends BaseAdapter {
    ArrayList<YoutubeItem> items = new ArrayList<>();   //유튜브 아이템들의 (어레이)리스트

    public void addItem(YoutubeItem item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        YoutubeItemView view;

        if (convertView == null) {
            view = new YoutubeItemView(parent.getContext());
        } else {
            view = (YoutubeItemView) convertView;
        }

        YoutubeItem item = items.get(position);
        view.setItemLink(item.getURL());
        view.setItemThumbnail(item.getThumbnail());
        view.setItemTitle(item.getTitle());
        view.setItemYoutuber(item.getChannel());
        //view.setItemDescription(item.getDescription());

        return view;
    }

}

