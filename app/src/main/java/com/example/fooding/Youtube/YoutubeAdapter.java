package com.example.fooding.Youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;

import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {
    ArrayList<YoutubeItem> items = new ArrayList<>();   //유튜브 아이템들의 (어레이)리스트
    private Context context;
    private View.OnClickListener onClickItem;

    public YoutubeAdapter(Context context, ArrayList<YoutubeItem> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.items = itemList;
        this.onClickItem = onClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
        .inflate(R.layout.youtube_item_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YoutubeItem item = items.get(position);

        holder.youtubeItemView.setItemLink(item.getURL());

        ImageLoadTask imageLoadTask = new ImageLoadTask(item.getThumbnail(), holder.youtubeThumbnailView);
        imageLoadTask.execute();
        holder.youtubeTitleView.setText(item.getTitle());
        holder.youtubeYoutuberView.setText(item.getChannel());
        holder.youtubeLinkButton.setOnClickListener(onClickItem);

    }

    @Override
    public int getItemCount() {
            return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public YoutubeItemView youtubeItemView;
        public ImageView youtubeThumbnailView;
        public TextView youtubeTitleView;
        public TextView youtubeYoutuberView;
        public Button youtubeLinkButton;


        public ViewHolder(View itemView) {
            super(itemView);

            youtubeItemView = new YoutubeItemView(context);
            youtubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail_view);
            youtubeTitleView = itemView.findViewById(R.id.youtube_title_view);
            youtubeYoutuberView = itemView.findViewById(R.id.youtube_youtuber_view);
            youtubeLinkButton = itemView.findViewById(R.id.youtube_link_button);

        }
    }

    public void addItem(YoutubeItem item){
        items.add(item);
    }

    @Override
    public long getItemId(int position) { return position; }


}

