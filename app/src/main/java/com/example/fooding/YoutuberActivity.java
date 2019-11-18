package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooding.Target.TargetList;
import com.example.fooding.Youtube.Top10YoutuberList;
import com.example.fooding.Youtuber.MyListDecoration;
import com.example.fooding.Youtuber.YoutuberAdapter;
import com.google.common.collect.Multiset;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class YoutuberActivity extends Search2Activity {
    TMapView tMapView;
    Intent intent;

    ArrayList<JSONObject> jsonObjectArrayList;
    ArrayList<TMapMarkerItem2> markerList;
    ArrayList<TargetList> targetListArray;

    private RecyclerView listview;
    private YoutuberAdapter adapter;
    TMapPoint centerPoint;

    Top10YoutuberList[] top10YoutuberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtuber);
        intent = getIntent();

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        layoutTmap.addView( tMapView );

        targetListArray = new ArrayList<TargetList>();
        targetListArray = intent.getParcelableArrayListExtra("targetListForYoutuberActivity");



        final double[] centerPointList = intent.getDoubleArrayExtra("point");
        TMapPoint centerPoint = new TMapPoint(centerPointList[0], centerPointList[1]);

        tMapView.setCenterPoint(centerPointList[1], centerPointList[0], true);
        TMapCircle tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( centerPoint );
        tMapCircle.setRadius(30);
        tMapCircle.setCircleWidth(15);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);
        tMapView.addTMapCircle("circle1", tMapCircle);

        //지도 이벤트 설정
            // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPress~!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
            // 롱 클릭 이벤트 설정
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                //Toast.makeText(MapEvent.this, "onLongPress~!", Toast.LENGTH_SHORT).show();
            }
        });
            // 지도 스크롤 종료
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                //Toast.makeText(getApplicationContext(), "zoomLevel=" + zoom + "\nlon=" + centerPoint.getLongitude() + "\nlat=" + centerPoint.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });

        //화면 설정
        Button settingButton = findViewById(R.id.setting_button);

        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);

        Button searchButton = findViewById(R.id.search_button);
        //Button youtuberButton = findViewById(R.id.youtuber_button);
        Button worldcupButton = findViewById(R.id.worldcup_button);
        Button likeButton = findViewById(R.id.like_button);

        //아래 toolbar 버튼 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        worldcupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });

        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });

        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        TMapPoint tMapPoint1 = tMapView.getCenterPoint();

        //youtuber listview 셋팅
        init(targetListArray);

    }

    private void init(ArrayList<TargetList> targetListArray) {

        listview = findViewById(R.id.youtuber_listview2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);

        youtuberFilter(targetListArray);

        ArrayList<String> itemList = new ArrayList<>();
        for(int i=0; i<10; i++) {
            itemList.add(top10YoutuberList[i].channelName);
        }
        //TODO top10YoutuberChannel 스트링배열이 상위 10명 유튜버 TargetList.YoutubeItems.Channel 명임




        adapter = new YoutuberAdapter(this, itemList, onClickItem);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);
    }

    private void youtuberFilter(ArrayList<TargetList> targetListArray){

        Map<String,Integer> tempArray = new HashMap<String,Integer>();

        //Hashmap 생성
        for(int i=0; i<targetListArray.size();i++){
            for(int j=0; j<targetListArray.get(i).youtubeItems.size(); j++){
                if(!(tempArray.containsKey(targetListArray.get(i).youtubeItems.get(j).channel))){
                    tempArray.put(targetListArray.get(i).youtubeItems.get(j).channel, 1);
                }
                else{
                    tempArray.put(targetListArray.get(i).youtubeItems.get(j).channel, tempArray.get(targetListArray.get(i).youtubeItems.get(j).channel)+1);
                }
            }
        }

        //Hashmap sorting
        List<Map.Entry<String, Integer>> list = new LinkedList<>(tempArray.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int comparision = (o1.getValue() - o2.getValue()) * -1;
                return comparision == 0 ? o1.getKey().compareTo(o2.getKey()) : comparision;
            }
        });

        // 순서유지를 위해 LinkedHashMap을 사용
        Map<String, Integer> sortedTempArray = new LinkedHashMap<>();
        for(Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();){
            Map.Entry<String, Integer> entry = iter.next();
            sortedTempArray.put(entry.getKey(), entry.getValue());
        }

        Iterator iterator = sortedTempArray.entrySet().iterator();

        top10YoutuberList = new Top10YoutuberList[10];
        for(int i=0; i< 10; i++){
            top10YoutuberList[i] = new Top10YoutuberList();
        }

        for(int i=0; i<10; i++){
            Map.Entry entry = (Map.Entry)iterator.next();
            top10YoutuberList[i].channelName = (String)entry.getKey();
        }

        for(int i=0; i<targetListArray.size(); i++){
            for(int j=0; j<targetListArray.get(i).youtubeItems.size(); j++) {
                for (int k = 0; k < top10YoutuberList.length; k++) {
                    if (targetListArray.get(i).youtubeItems.get(j).channel == top10YoutuberList[k].channelName)
                        top10YoutuberList[k].resNameList.add(targetListArray.get(i).name);
                }

            }
        }
    }

    public void showYoutuberMarker(ArrayList<TMapMarkerItem2> markerList, TMapPoint centerPoint, Top10YoutuberList youtuber) {
        TMapMarkerItem2 marker = null;
        for (int i = 0; i < markerList.size(); i++) {
            for(int j=0; j<youtuber.resNameList.size(); j++) {
                if (youtuber.resNameList.get(j) == markerList.get(i).getID()) {
                    marker = markerList.get(i);
                    tMapView.addMarkerItem2(marker.getID(), marker);    // 지도에 추가
                }
            }
        }
    }


    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            //v.setBackgroundResource(R.drawable.radius_background_black);
            TextView textView = (TextView) v;

            //adapter.setInitial();

            textView.setBackgroundResource(R.drawable.radius_background_black);
            textView.setTextColor(getResources().getColor(android.R.color.white));
            Toast.makeText(YoutuberActivity.this, str, Toast.LENGTH_SHORT).show();

            for(int i=0; i<top10YoutuberList.length; i++){
                if(top10YoutuberList[i].channelName == str){
                    showYoutuberMarker(markerList, centerPoint, top10YoutuberList[i]);
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "channel", Toast.LENGTH_SHORT).show();
    }
}
