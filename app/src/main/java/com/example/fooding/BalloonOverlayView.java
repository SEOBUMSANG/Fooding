package com.example.fooding;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fooding.Youtube.ResponseInfo;
import com.example.fooding.Youtube.YoutubeAdapter;
import com.example.fooding.Youtube.YoutubeItem;
import com.example.fooding.Youtube.YoutubeList;
import com.google.gson.Gson;

public class BalloonOverlayView extends FrameLayout {

    private LinearLayout layout;
    private TextView title;
    private TextView subTitle;

    String id;
    YoutubeList youtubeList;
    YoutubeAdapter adapter;

    public BalloonOverlayView(Context context, String labelName, String id) {
        super(context);
        this.id = id;
        adapter = new YoutubeAdapter();

        setPadding(10, 0, 10, 0);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);

        //requestYoutubeList(this.id); // db에 유튜브 리스트 요청
        setupView(context, layout, labelName, id);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.NO_GRAVITY;
        addView(layout, params);
    }


    protected void setupView(Context context, final ViewGroup parent, String labelName, String id) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.youtube_list_view, parent, true);

        //title = (TextView) view.findViewById(R.id.bubble_title);
        //subTitle = (TextView) view.findViewById(R.id.bubble_subtitle);

        setTitle(labelName);
        setSubTitle(id);

    }

    public void requestYoutubeList(final String restaurantName) {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + AppHelper.readCommentList;
        url += "?" + "name=" + restaurantName;

        StringRequest request = new StringRequest(
                Request.Method.GET,
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

        try {
            request.setShouldCache(false);
            AppHelper.requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            youtubeList = gson.fromJson(response, YoutubeList.class);

            setYoutubeList();
        }
    }

    public void setYoutubeList() {
        for (int i = 0; i < youtubeList.result.size(); i++) {
            YoutubeItem youtubeItem = youtubeList.result.get(i);
            adapter.addItem(youtubeItem);
        }
        adapter.notifyDataSetChanged();
    }


    public void setTitle(String str) {
        //title.setText(str);
    }

    public void setSubTitle(String str) {
        //subTitle.setText(str);
    }
}
