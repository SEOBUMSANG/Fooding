package com.example.fooding;

import android.content.Context;
import android.util.Log;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooding.Target.TargetList;
import com.example.fooding.Youtube.YoutubeAdapter;
import com.example.fooding.Youtube.YoutubeItem;
import com.example.fooding.Youtube.YoutubeItemDecoration;
import com.google.gson.Gson;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class BalloonOverlayView extends FrameLayout {

    private LinearLayout layout;
    private TextView title;

    RecyclerView listView;

    //refactorJS
    YoutubeItem[] youtubeItems;
    ArrayList<YoutubeItem> items = new ArrayList<>();

    YoutubeAdapter adapter;

    TMapPoint markerPoint;
    String uri[];


    public BalloonOverlayView(Context context, TargetList eachTarget) {
        super(context);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 279, getResources().getDisplayMetrics());

        setPadding(10, 0, 10, 0);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);


        // 좌표 만들기
        markerPoint = new TMapPoint(Double.parseDouble(eachTarget.lat), Double.parseDouble(eachTarget.lng));
        // 뷰 설정
        setupView(context, layout, eachTarget.name);


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);

        adapter = new YoutubeAdapter(getContext(), items, onClickItem);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        YoutubeItemDecoration decoration = new YoutubeItemDecoration();
        listView.addItemDecoration(decoration);

    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }
    };

    /*refactorJS
    public boolean processResponse(String response) {
        Gson gson = new Gson();

        targetList = gson.fromJson(response, TargetList.class);
        youtubeItems = gson.fromJson(targetList.youtube, YoutubeItem[].class);

        setYoutubeList();

        return true;
    }*/

    public void setYoutubeList() {
        for (int i = 0; i < youtubeItems.length; i++) {
            YoutubeItem youtubeItem = youtubeItems[i];
            items.add(youtubeItem);
        }
    }

    public void setTitle(String str) {
        title.setText(str);
    }

}
