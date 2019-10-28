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
import com.example.fooding.Youtube.YoutubeAdapter;
import com.example.fooding.Youtube.YoutubeItem;
import com.example.fooding.Youtube.YoutubeList;
import com.google.gson.Gson;
import com.skt.Tmap.TMapPoint;

public class BalloonOverlayView extends FrameLayout {

    private LinearLayout layout;
    private TextView title;

    ListView listView;

    YoutubeList youtubeList;
    YoutubeAdapter adapter;

    TMapPoint markerPoint;

    public BalloonOverlayView(Context context, String response) {
        super(context);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());

        adapter = new YoutubeAdapter();

        setPadding(10, 0, 10, 0);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);

        // json 파싱
        if ( processResponse(response) ) {
            // 좌표 만들기
            markerPoint = new TMapPoint(youtubeList.lat, youtubeList.lng);

            // 뷰 설정
            setupView(context, layout, youtubeList.name);
        }

        // 풍선뷰 크기
        LayoutParams param = new LayoutParams(width, height);
        param.gravity = Gravity.NO_GRAVITY;
        addView(layout, param);
    }


    protected void setupView(Context context, final ViewGroup parent, String name) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.youtube_list_view, parent, true);

        listView = view.findViewById(R.id.listView);
        title = view.findViewById(R.id.list_title_view);

        // 음식점 이름 설정
        setTitle(name);

        for (int i = 0; i < youtubeList.youtube.size(); i++) {
            YoutubeItem youtubeItem = youtubeList.youtube.get(i);
            adapter.addItem(youtubeItem);
        }
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

    }


    public boolean processResponse(String response) {
        Gson gson = new Gson();

        youtubeList = gson.fromJson(response, YoutubeList.class);

        setYoutubeList();

        if (youtubeList.youtube.isEmpty()) {
            return false;
        }
        return true;
    }

    public void setYoutubeList() {
        for (int i = 0; i < youtubeList.youtube.size(); i++) {
            YoutubeItem youtubeItem = youtubeList.youtube.get(i);
            adapter.addItem(youtubeItem);
        }
        adapter.notifyDataSetChanged();
    }


    public void setTitle(String str) {
        title.setText(str);
    }

}
