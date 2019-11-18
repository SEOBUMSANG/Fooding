package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fooding.Target.TargetList;

import com.example.fooding.Youtube.Top10YoutuberList;
import com.example.fooding.Youtube.YoutubeItem;
import com.example.fooding.Youtuber.MyListDecoration;
import com.example.fooding.Youtuber.YoutuberAdapter;
import com.google.gson.Gson;
import com.skt.Tmap.TMapCircle;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.location.Address;
import org.json.JSONObject;


public class Search2Activity extends MapActivity {
    TMapView tMapView;
    Intent intent;

    CurrentGps currentGps;
    ArrayList<JSONObject> jsonObjectArrayList;
    ArrayList<TMapMarkerItem> bigMarkerList;
    static ArrayList<TMapMarkerItem2> markerList;
    ArrayList<TMapMarkerItem2> partMarkerList;

    TargetList[] targetList;
    ArrayList<TargetList> targetListForIntent;

    SearchDB searchDB;

    boolean bigMode = true;
    boolean youtuberMode = false;
    boolean worldcupMode = false;
    float[] dist = new float[1];

    //Youtuber Activity
    private RecyclerView youtuberListview;
    private YoutuberAdapter adapter;
    Top10YoutuberList[] top10YoutuberList;
    boolean[] checkClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        Intent getIntent = getIntent();
        jsonObjectArrayList = new ArrayList<>();
        bigMarkerList = new ArrayList<>();
        markerList = new ArrayList<>();
        partMarkerList = new ArrayList<>();
        final double[] centerPointList = getIntent.getDoubleArrayExtra("point");
        final TMapPoint centerPoint = new TMapPoint(centerPointList[0], centerPointList[1]);

        checkClicked = new boolean[10];

        init(centerPoint);

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        layoutTmap.addView( tMapView );

        tMapView.setCenterPoint(centerPointList[1], centerPointList[0], true);
        TMapCircle tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( centerPoint ); // 센터 설정
        tMapCircle.setRadius(30);
        tMapCircle.setCircleWidth(15);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);
        tMapView.addTMapCircle("circle1", tMapCircle);

        //월드컵용 가운데 마커
        final TMapMarkerItem centerMarkerItem = new TMapMarkerItem();
        final String sID = "centerMarkerItem";
        centerMarkerItem.setID(sID); // 마커의 id 지정
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_green);
        centerMarkerItem.setIcon(resizeBitmap(bitmap, 150)); // 마커 아이콘 지정
        centerMarkerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정


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


        tMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
            @Override
            public void onEnableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {

            }
        });
        // 지도 스크롤 종료
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                if (youtuberMode) {

                } else {
                    boolean result = false;
                    for(int i =0;i<markerList.size();i++){
                        result = markerList.get(i).getMarkerTouch();
                        if (result == true){
                            markerList.get(i).setMarkerTouch(false);
                        }
                    }

                    if(bigMode) {
                        deleteMarker(tMapView, bigMarkerList);
                        showMarker(bigMarkerList, centerPoint);
                    }
                    else{
                        deleteMarker2(tMapView, markerList);
                        showMarker2(markerList, centerPoint);
                    }

                    if(worldcupMode) {
                        centerMarkerItem.setTMapPoint(tMapView.getCenterPoint());
                    }
                }
            }
        });
        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String s, TMapMarkerItem2 tMapMarkerItem2) {
                Toast.makeText(getApplicationContext(), "marker", Toast.LENGTH_SHORT).show();
                MarkerOverlay marker = (MarkerOverlay) tMapMarkerItem2;

                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.balloonView.items.get(0).URL));
                startActivity(myintent);
            }
        });

        //화면 설정
        Button settingButton = findViewById(R.id.setting_button);
        Button mylocationButton = findViewById(R.id.my_location_button);
        final LinearLayout linearLayoutLocationInput = findViewById(R.id.linear_layout_location_input);
        final EditText locationInput = findViewById(R.id.location_input);
        Button locationInputButton = findViewById(R.id.location_input_button);

        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);
        final Button worldcupStartButton = findViewById(R.id.worldcup_start_button);

        final Button searchButton = findViewById(R.id.search_button);
        final RelativeLayout layoutSearchButton = findViewById(R.id.layout_search_button);
        final Button youtuberButton = findViewById(R.id.youtuber_button);
        final RelativeLayout layoutYoutuberButton = findViewById(R.id.layout_youtuber_button);
        final Button worldcupButton = findViewById(R.id.worldcup_button);
        final RelativeLayout layoutWorldcupButton = findViewById(R.id.layout_worldcup_button);
        final Button likeButton = findViewById(R.id.like_button);
        final RelativeLayout layoutLikeButton = findViewById(R.id.layout_like_button);


        //현재위치 버튼
        mylocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGps = new CurrentGps(Search2Activity.this);
                // GPS 사용유무 가져오기
                if (currentGps.isGetLocation()) {

                    double latitude = currentGps.getLatitude();
                    double longitude = currentGps.getLongitude();

                    tMapView.setCenterPoint(longitude,latitude,true);

                    Toast.makeText(
                            getApplicationContext(),
                            "위도: " + latitude + " 경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    // GPS 를 사용할수 없으므로
                    currentGps.showSettingsAlert();
                }
            }
        });

        //월드컵 시작 버튼
        worldcupStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldcupStart();
            }
        });
        //아래 toolbar 버튼 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youtuberMode) {
                    youtuberMode = false;

                    showMarker(bigMarkerList, centerPoint);

                    layoutYoutuberButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                    youtuberListview.setVisibility(View.INVISIBLE);
                    linearLayoutLocationInput.setVisibility(View.VISIBLE);
                }
                if (worldcupMode) {
                    worldcupMode = false;

                    tMapView.removeMarkerItem(centerMarkerItem.getID());

                    layoutWorldcupButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                    worldcupStartButton.setVisibility(View.INVISIBLE);
                }

                layoutSearchButton.setBackgroundResource(R.drawable.oval_background_orange_fill);

            }
        });

        youtuberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtuberMode = true;

                //위치검색창 invisible
                linearLayoutLocationInput.setVisibility(View.INVISIBLE);
                //리스트뷰 visible
                youtuberListview.setVisibility(View.VISIBLE);
                //서치 버튼 레이아웃 blank
                layoutSearchButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                //유튜버 버튼 레이아웃 fill
                layoutYoutuberButton.setBackgroundResource(R.drawable.oval_background_orange_fill);
                //지도 위의 마커 다 삭제
                tMapView.removeAllMarkerItem();

                if (worldcupMode) {
                    worldcupStartButton.setVisibility(View.INVISIBLE);
                    layoutWorldcupButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                }
            }
        });
        worldcupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldcupMode = true;

                layoutSearchButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                layoutWorldcupButton.setBackgroundResource(R.drawable.oval_background_orange_fill);
                worldcupStartButton.setVisibility(View.VISIBLE);

                centerMarkerItem.setTMapPoint(tMapView.getCenterPoint());
                tMapView.addMarkerItem(centerMarkerItem.getID(), centerMarkerItem);

                if (youtuberMode)
                    layoutYoutuberButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 위치 검색, 이동
        locationInputButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String address = locationInput.getText().toString();
                Geocoder geocoder = new Geocoder(getApplicationContext());

                ArrayList<GeoLocation> resultList = new ArrayList<>();

                try {
                    List<Address> list = geocoder.getFromLocationName(address, 10);

                    for (Address addr : list) {
                        resultList.add(new GeoLocation(addr.getLatitude(), addr.getLongitude()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tMapView.setCenterPoint(resultList.get(0).longitude, resultList.get(0).latitude, true);
            }
        });

        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
                Log.v("MapZoomIn", "zoomlevel : " + tMapView.getZoomLevel());
                if (tMapView.getZoomLevel() == 15) {
                    parseBigMarker(tMapView.getCenterPoint());
                    bigMode = false;
                }
            }
        });

        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
                Log.v("MapZoomOut", "zoomlevel : " + tMapView.getZoomLevel());
                if (tMapView.getZoomLevel() == 16) {
                    mergeMarker(tMapView.getCenterPoint());
                    bigMode = true;
                }
            }
        });

        // 시작
        // 잠시 시간 필요함
        if(markerList.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("getTargeList 시작 전", "시작 전");
                    try {
                        getTargetList(jsonObjectArrayList);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Log.e("getTargeList 완료", "완료");

                    targetListForIntent = new ArrayList<>();
                    for(int i = 0 ; i<targetList.length; i++){
                        targetListForIntent.add(targetList[i]);
//                        if ( !targetListForIntent.get(i).resImageUrlList.isEmpty() )
//                            Log.i("intent용 target으로 옮길 때 ", "" + targetListForIntent.get(i).resImageUrlList.get(0) );
                    }

                    if (makeBigMarker(targetList, bigMarkerList)) {    // 마커 생성
                        showMarker(bigMarkerList, centerPoint);
                        makeMarker(targetList, markerList);
                    }

                    initYoutuber(targetListForIntent);
                }
            }, 5000);
        }


    }

    public void init(final TMapPoint centerPoint) {
        // 전체 음식점 정보 json으로 받아오기
        Log.w("init", "init");
        searchDB = new SearchDB();
        if (jsonObjectArrayList.isEmpty()) {
            Log.w("init", "jsonObjectArrayList 비어있어서 searchDB.returnData");
            searchDB.returnData(jsonObjectArrayList, centerPoint);
        }
    }

    // db에 유튜브 리스트 요청 및 정제 refactorJS
    public void getTargetList(ArrayList<JSONObject> jsonObjectArrayList) throws MalformedURLException {
        Gson gson = new Gson();
        JSONObject jsonObject;

        YoutubeItem[] temptubeItems;
        String[] tempUrls;
        targetList = new TargetList[jsonObjectArrayList.size()];
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            String response = jsonObject.toString();

            targetList[i] = gson.fromJson(response, TargetList.class);
            targetList[i].youtubeItems = new ArrayList<>();
            targetList[i].resImageUrlList = new ArrayList<>();

            temptubeItems = gson.fromJson(targetList[i].youtube, YoutubeItem[].class);
            tempUrls = gson.fromJson(targetList[i].resImageURL, String[].class);

            for (int j = 0; j < temptubeItems.length; j++) {
                targetList[i].youtubeItems.add(temptubeItems[j]);
            }
            //에러 발생 부분
//            for (int j = 0; j < tempUrls.length; j++) {
//                if (URLUtil.isValidUrl( tempUrls[j] ) ) {
//                    targetList[i].resImageUrlList.add( tempUrls[j] );
//                }
//            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void mergeMarker(TMapPoint centerPoint) {
        Log.d("mergeMarker", "delete Marker & makeBigMarker");

        deleteMarker2(tMapView, markerList);

//        if (partMarkerList.isEmpty()){
            showMarker(bigMarkerList,centerPoint);
//        } else {
//            showMarker(partMarkerList);
//        }

    }

    public void parseBigMarker(TMapPoint centerPoint) {
        Log.d("parseBigMarker", "delete BigMarker & makeMarker");

        deleteMarker(tMapView, bigMarkerList);

//        if (partMarkerList.isEmpty()){
            showMarker2(markerList, centerPoint);
//        } else {
//            showMarker(partMarkerList);
//        }

    }

    public void showMarker(ArrayList<TMapMarkerItem> markerList, TMapPoint centerPoint) {
        TMapMarkerItem marker = null;
        for (int i = 0; i < markerList.size(); i++) {
            Location.distanceBetween(markerList.get(i).latitude,markerList.get(i).longitude,centerPoint.getLatitude(),centerPoint.getLongitude(),dist);

            if (bigMode == true) {
                if (dist[0] > 2000)
                    continue;
                marker = markerList.get(i);
                tMapView.addMarkerItem(marker.getID(), marker);    // 지도에 추가
            } else {
                if(dist[0]>500){
                    continue;
                }
                marker = markerList.get(i);
                tMapView.addMarkerItem(marker.getID(), marker);    // 지도에 추가
            }
        }
    }


    public void showMarker2(ArrayList<TMapMarkerItem2> markerList, TMapPoint centerPoint) {
        TMapMarkerItem2 marker = null;
        for (int i = 0; i < markerList.size(); i++) {
            Location.distanceBetween(markerList.get(i).latitude,markerList.get(i).longitude,centerPoint.getLatitude(),centerPoint.getLongitude(),dist);

            if (bigMode == true) {
                if (dist[0] > 2000)
                    continue;
                marker = markerList.get(i);
                tMapView.addMarkerItem2(marker.getID(), marker);    // 지도에 추가
            } else {
                if(dist[0]>500){
                    continue;
                }
                marker = markerList.get(i);
                tMapView.addMarkerItem2(marker.getID(), marker);    // 지도에 추가
            }
        }
    }

    public void worldcupStart() {
        intent = new Intent(getApplicationContext(), WorldcupActivity.class);
        intent.putExtra("lat", tMapView.getCenterPoint().getLatitude());
        intent.putExtra("lng", tMapView.getCenterPoint().getLongitude());
        intent.putExtra("targetList", targetListForIntent);

        startActivityForResult(intent, 203);
    }

    private void initYoutuber(ArrayList<TargetList> targetListArray) {

        youtuberListview = findViewById(R.id.youtuber_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        youtuberListview.setLayoutManager(layoutManager);

        youtuberFilter(targetListArray);

        ArrayList<String> itemList = new ArrayList<>();
        for(int i=0; i<10; i++) {
            itemList.add(top10YoutuberList[i].channelName);
        }
        //TODO top10YoutuberChannel 스트링배열이 상위 10명 유튜버 TargetList.YoutubeItems.Channel 명임


        adapter = new YoutuberAdapter(this, itemList, onClickItem);
        youtuberListview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        youtuberListview.addItemDecoration(decoration);
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
        TMapMarkerItem2 marker;
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
            //v.setBackgroundResource(R.drawable.radius_background_black);
            YoutuberAdapter.ViewHolder textViewList;
            TextView textView = (TextView) v;
            TextView clickedTextView;
            String str = (String)textView.getText();
            int position = Integer.parseInt(textView.getTag().toString());
            int clickedPosition = -1;
            //adapter.setInitial();



            for(int i=0; i<top10YoutuberList.length; i++){
                if(top10YoutuberList[i].channelName == str){
//잠시 주석처리              //showYoutuberMarker(markerList, centerPoint, top10YoutuberList[i]);
                }
            }

            for(int i = 0 ;i<10;i++){
                if(checkClicked[i]==true){
                    clickedPosition = i;
                }
            }

            if(clickedPosition!=-1) {
                textViewList = (YoutuberAdapter.ViewHolder) youtuberListview.findViewHolderForLayoutPosition(clickedPosition);
                clickedTextView = textViewList.textview;
                clickedTextView.getText();
                clickedTextView.setBackgroundResource(R.drawable.radius_background_orange);
                clickedTextView.setTextColor(getResources().getColor(android.R.color.black));
                checkClicked[clickedPosition] = false;
            }

            if(position != clickedPosition) {
                textView.setBackgroundResource(R.drawable.radius_background_black);
                textView.setTextColor(getResources().getColor(android.R.color.white));
                checkClicked[position] = true;
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }

        }
    };

}
