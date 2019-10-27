package com.example.fooding;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    ListView listView;
    YoutubeList youtubeList;
    YoutubeAdapter adapter;

    public BalloonOverlayView(Context context, String labelName, String id) {
        super(context);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());

        adapter = new YoutubeAdapter();

        setPadding(10, 0, 10, 0);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);

        //requestYoutubeList(this.id); // db에 유튜브 리스트 요청
        setupView(context, layout, labelName, id);

        //LayoutParams params = new LayoutParams( , LayoutParams.WRAP_CONTENT);
        LayoutParams param = new LayoutParams(width, height);
        param.gravity = Gravity.NO_GRAVITY;
        addView(layout, param);
    }


    protected void setupView(Context context, final ViewGroup parent, String labelName, String id) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.youtube_list_view, parent, true);

        listView = view.findViewById(R.id.listView);
        //title = (TextView) view.findViewById(R.id.bubble_title);
        //subTitle = (TextView) view.findViewById(R.id.bubble_subtitle);

        setTitle(labelName);
        setSubTitle(id);

        //requestYoutubeList(id);

        //테스트
        YoutubeItem youtubeItem = new YoutubeItem(
                "https://i.ytimg.com/vi/upr-gMBSsQE/default.jpg",
                "쿠캣 - COOKAT",
                "강남에 상륙한 대만 삼미식당 리얼후기     대왕연어초밥에 장어덮밥까지 싹 다 먹어봄! 추천 메뉴는 꼭 먹어봐야 합니다,, ",
                "[오늘 뭐 먹지?] 강남에 상륙한 대만 삼미식당 리얼후기\uD83D\uDD25\uD83D\uDD25",
                "https://www.youtube.com/watch?v=upr-gMBSsQE");
        adapter.addItem(youtubeItem);
        adapter.notifyDataSetChanged();
        //

        listView.setAdapter(adapter);

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
        for (int i = 0; i < youtubeList.youtube.size(); i++) {
            YoutubeItem youtubeItem = youtubeList.youtube.get(i);
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
