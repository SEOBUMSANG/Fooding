package com.example.fooding.Youtube;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fooding.AppHelper;
import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;
import com.google.gson.Gson;

public class YoutubeItemView extends LinearLayout {
    int youtubeId;
    String youtubeLink;
    ImageView youtubeThumbnailView;
    TextView youtubeTitleView;
    TextView youtubeYoutuberView;
    TextView youtubeHitsView;
    TextView youtubeDateView;
    Button youtubeLinkButton;

    //RecommendCallback recommendCallback;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_item_view, this, true);

        youtubeThumbnailView = findViewById(R.id.youtube_thumbnail_view);
        youtubeTitleView = findViewById(R.id.youtube_title_view);
        youtubeYoutuberView = findViewById(R.id.youtube_youtuber_view);
        youtubeHitsView = findViewById(R.id.youtube_hits_view);
        youtubeDateView = findViewById(R.id.youtube_date_view);
        youtubeLinkButton = findViewById(R.id.youtube_link_button);
    }

    public void setItemId(int id) {youtubeId = id;}
    public void setItemLink(String link) {youtubeLink = link;}
    public void setItemThumbnail(String thumbnail) {
        ImageLoadTask imageLoadTask = new ImageLoadTask(thumbnail, youtubeThumbnailView);
        imageLoadTask.execute();
    }
    public void setItemTitle(String title) {youtubeTitleView.setText(title);}
    public void setItemYoutuber(String youtuber) {youtubeYoutuberView.setText(youtuber);}
    public void setItemHits(int hits) {youtubeHitsView.setText(toString().valueOf(hits));}
    public void setItemDate(String date) {youtubeDateView.setText(date);}

    //포맷 남겨둠
    public void requestIncreaseRecommend(final int id) {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseRecommend";
        url += "?" + "review_id=" + id;

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,    //GET 방식은 요청 path가 필요
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "에러발생", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }

    public void processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            try {
                //recommendCallback.resetComment();
                Toast.makeText(getContext(), "추천 완료", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
