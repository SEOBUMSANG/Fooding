package com.example.fooding;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.fooding.Target.TargetList;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    ArrayList<TargetList> items = new ArrayList<>();

    public void addItem(TargetList item){
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
        CommentItemView view;
        if (convertView == null) {
            view = new CommentItemView(parent.getContext());
        } else {
            view = (CommentItemView) convertView;
        }

        TargetList item = items.get(position);

        //view.setItemId(item.getName());
        view.setItemImage(/*item.getResId()*/);
        view.setItemName(item.getName());
        view.setItemDescription(item.getDescription());
        view.setItemAddress(item.getResAddress());

        return view;
    }

}

