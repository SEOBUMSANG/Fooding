package com.example.fooding.Youtuber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fooding.R;

import java.util.ArrayList;

public class YoutuberAdapter extends RecyclerView.Adapter<YoutuberAdapter.ViewHolder> {
    private View view;
    private ArrayList<String> itemList;
    private Context context;
    private View.OnClickListener onClickItem;

    public YoutuberAdapter(Context context, ArrayList<String> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.youtuber_item, parent, false);

        this.view = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemList.get(position);

        holder.textview_name.setText(item);
        holder.textview.setText(item.substring(0,1));
        holder.textview.setTag(position + item);
        holder.textview.setOnClickListener(onClickItem);

    }

//    public void setInitial() {
//        ViewHolder holder = onCreateViewHolder()
//
//        for(int i=0; i<getItemCount(); i++) {
//            holder.textview.setBackgroundResource(R.drawable.radius_background_orange);
//        }
//    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textview;
        public TextView textview_name;

        public ViewHolder(View itemView) {
            super(itemView);

            textview = itemView.findViewById(R.id.item_textview);
            textview_name = itemView.findViewById(R.id.item_name);
        }
    }
}

