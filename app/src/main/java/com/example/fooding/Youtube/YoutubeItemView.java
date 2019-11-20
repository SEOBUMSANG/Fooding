package com.example.fooding.Youtube;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;



public class YoutubeItemView extends LinearLayout {
    Context mcontext;

    int youtubeId;
    String youtubeLink;
    ImageView youtubeThumbnailView;
    TextView youtubeTitleView;
    TextView youtubeYoutuberView;
    //TextView youtubeDescriptionView;
    Button youtubeLinkButton;

    public YoutubeItemView(Context context) {
        super(context);
        init(context);

//        if (context instanceof RecommendCallback) {
//            recommendCallback = (RecommendCallback) context;
//        }
    }

    public YoutubeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

//        if (recommendCallback != null) {
//            recommendCallback = null;
//        }
    }

    private void init(Context context) {
        mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_item_view, this, true);

        youtubeThumbnailView = findViewById(R.id.youtube_thumbnail_view);
        youtubeTitleView = findViewById(R.id.youtube_title_view);
        youtubeYoutuberView = findViewById(R.id.youtube_youtuber_view);
        //youtubeDescriptionView = findViewById(R.id.youtube_description_view);
        youtubeLinkButton = findViewById(R.id.youtube_link_button);

        youtubeLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //링크 열기
//                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
//                mcontext.startActivity(myintent);
            }
        });
    }

    public void setItemId(int id) {youtubeId = id;}
    public void setItemLink(String link) {youtubeLink = link;}
    public void setItemThumbnail(String thumbnail) {
        ImageLoadTask imageLoadTask = new ImageLoadTask(thumbnail, youtubeThumbnailView);
        imageLoadTask.execute();
    }
    public void setItemTitle(String title) {youtubeTitleView.setText(title);}
    public void setItemYoutuber(String youtuber) {youtubeYoutuberView.setText(youtuber);}
    //public void setItemDescription(String description) {youtubeDescriptionView.setText(description);}

}
