package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
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
import com.google.api.LogDescriptor;
import com.google.gson.Gson;
import com.skt.Tmap.TMapCircle;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
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
    SearchDB searchDB;
    Global global;
    CurrentGps currentGps;

    ArrayList<TMapMarkerItem2> activeMarkerList;
    ArrayList<TMapMarkerItem> bigMarkerList;
    Top10YoutuberList[] top10YoutuberList;

    boolean bigMode = true;
    boolean youtuberMode = false;
    boolean worldcupMode = false;
    boolean likeMode = false;


    //Youtuber Activity
    private RecyclerView youtuberListview;
    private YoutuberAdapter adapter;
    private TMapCircle tMapCircle;
    boolean[] checkClicked;
    String guName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        Intent getIntent = getIntent();
        guName = getIntent.getStringExtra("guName");

        final double[] centerPointList = getIntent.getDoubleArrayExtra("point");
        final TMapPoint centerPoint = new TMapPoint(centerPointList[0], centerPointList[1]);

        bigMarkerList = new ArrayList<>();

        checkClicked = new boolean[10];
        activeMarkerList = new ArrayList<>();
        global= ((Global)getApplicationContext());


        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        tMapView.setCenterPoint(centerPointList[1], centerPointList[0], true);
        layoutTmap.addView( tMapView );


        //화면 설정
        Button mylocationButton = findViewById(R.id.my_location_button);
        Button locationInputButton = findViewById(R.id.location_input_button);
        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);

        final LinearLayout linearLayoutLocationInput = findViewById(R.id.linear_layout_location_input);
        final EditText locationInput = findViewById(R.id.location_input);

        final RelativeLayout layoutSearchButton = findViewById(R.id.layout_search_button);
        final RelativeLayout layoutYoutuberButton = findViewById(R.id.layout_youtuber_button);
        final RelativeLayout layoutWorldcupButton = findViewById(R.id.layout_worldcup_button);
        final RelativeLayout layoutLikeButton = findViewById(R.id.layout_like_button);

        final Button worldcupStartButton = findViewById(R.id.worldcup_start_button);
        final Button searchButton = findViewById(R.id.search_button);
        final Button youtuberButton = findViewById(R.id.youtuber_button);
        final Button worldcupButton = findViewById(R.id.worldcup_button);
        final Button likeButton = findViewById(R.id.like_button);

        //init();
        //getTargetList(global.getJsonObjectArrayList());
        initYoutuber(global.getTargetListArray());


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
                boolean result;
                for (int i = 0; i < global.getMarkerList().size(); i++) {
                    result = global.getMarkerList().get(i).getMarkerTouch();
                    if (result == true) {
                        global.getMarkerList().get(i).setMarkerTouch(false);
                    }
                }
            }
        });
            // 지도 스크롤 종료
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                //마커가 클릭되서 말풍선이 떠있는지 감지
                boolean markerClicked = false;

                Iterator<TMapMarkerItem2> itr = global.getMarkerList().iterator();
                while(itr.hasNext()) {
                    if (itr.next().getMarkerTouch()) {
                        markerClicked = true;
                        break;
                    }
                    else{
                        markerClicked = false;
                    }
                }


                if (!youtuberMode && !likeMode && !markerClicked) {
                    Log.w("setOnDisableScroll", "마커 갱신");
                    // 유튜버 모드일 때는 빅모드 지원 안함
                    if (bigMode) {
                        deleteMarker(tMapView, bigMarkerList);
                        showMarker(bigMarkerList, centerPoint, bigMode, tMapView);
                    } else {
                        deleteMarker2(tMapView, global.getMarkerList());
                        showMarker2(global.getMarkerList(), centerPoint, bigMode, tMapView);
                    }
                }

                if(worldcupMode) {
                    setWorldcupCircle();
                }


            }
        });
        //마커클릭이벤트
        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String s, TMapMarkerItem2 tMapMarkerItem2) {
                int likeIndex = 0;
                Log.w("setOnMarkerClick", "말풍선 클릭");
                MarkerOverlay marker = (MarkerOverlay) tMapMarkerItem2;

                for(likeIndex=0;likeIndex<global.getlikeList().size();likeIndex++) {
                    if(marker.getID().equals(global.getlikeList().get(likeIndex)))
                        break;
                }

                if (likeIndex == global.getlikeList().size()) {
                    global.setLikeList(marker.getID());
                }
                Log.d("추가된 라이크리스트"," "+marker.getID());
            }

        });
            // 마커 클릭 이벤트
//        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
//            @Override
//            public void onCalloutMarker2ClickEvent(String s, TMapMarkerItem2 tMapMarkerItem2) {
//                int likeIndex = 0;
//                Log.w("setOnMarkerClick", "말풍선 클릭");
//                MarkerOverlay marker = (MarkerOverlay) tMapMarkerItem2;
//
//                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.balloonView.items.get(0).URL));
//                startActivityForResult(myintent, 001);
//
//                for(likeIndex=0;likeIndex<global.getlikeList().size();likeIndex++) {
//                    if(marker.getID().equals(global.getlikeList().get(likeIndex)))
//                        break;
//                }
//
//                if(likeIndex == global.getlikeList().size()){
//                    global.setLikeList(marker.getID());
//                }
//                Log.d("추가된 라이크리스트"," "+marker.getID());
//            }
//
//        });
//        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
//            @Override
//            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
//                Log.w("CalloutRightButtonClick", "말풍선 롱클릭1");
//                int likeIndex = 0;
//                MarkerOverlay markerOverlay;
//                for (TMapMarkerItem2 marker : global.getMarkerList()) {
//                    if (marker.getMarkerTouch()) {
//                        markerOverlay = (MarkerOverlay) marker;
//
//                        Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(markerOverlay.balloonView.items.get(1).URL));
//                        startActivityForResult(myintent, 001);
//
//                        for(likeIndex=0;likeIndex<global.getlikeList().size();likeIndex++) {
//                            if(marker.getID().equals(global.getlikeList().get(likeIndex)))
//                                break;
//                        }
//
//                        if(likeIndex == global.getlikeList().size()){
//                            global.setLikeList(marker.getID());
//                        }
//                        Log.d("추가된 라이크리스트"," "+marker.getID());
//                    }
//
//                }
//            }
//        });

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

                    showMarker(bigMarkerList, centerPoint, bigMode, tMapView);

                    youtuberButton.setBackgroundResource(R.drawable.youtuber_icon);
                    youtuberListview.setVisibility(View.INVISIBLE);
                    linearLayoutLocationInput.setVisibility(View.VISIBLE);

                    //다시 서치모드로 전환 시 마커 삭제
                    tMapView.removeAllMarkerItem();
                    activeMarkerList = tMapView.getAllMarkerItem2();
                    for(int i =0;i<activeMarkerList.size();i++){
                        tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                    }
                }


                tMapView.removeAllTMapCircle();

                if (worldcupMode) {
                    worldcupMode = false;

                    worldcupButton.setBackgroundResource(R.drawable.new_blank_trophy);
                    worldcupStartButton.setVisibility(View.INVISIBLE);
                }

                if (likeMode) {
                    likeMode = false;

                    showMarker(bigMarkerList, centerPoint, bigMode, tMapView);

                    likeButton.setBackgroundResource(R.drawable.ic_playlist_new_1);
                    linearLayoutLocationInput.setVisibility(View.VISIBLE);
                    worldcupStartButton.setVisibility(View.INVISIBLE);

                    //다시 서치모드로 전환 시 마커 삭제
                    tMapView.removeAllMarkerItem();
                    activeMarkerList = tMapView.getAllMarkerItem2();
                    for(int i =0;i<activeMarkerList.size();i++){
                        tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                    }
                }

                //다시 서치모드로 전환 시 마커 띄워줌
                if (bigMode) {
                    showMarker(bigMarkerList, tMapView.getCenterPoint(), bigMode, tMapView);
                } else {
                    showMarker2(global.getMarkerList(), tMapView.getCenterPoint(), bigMode, tMapView);
                }

                searchButton.setBackgroundResource(R.drawable.new_fill_home);
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
                searchButton.setBackgroundResource(R.drawable.new_blank_home);
                //유튜버 버튼 레이아웃 fill
                youtuberButton.setBackgroundResource(R.drawable.youtuber_fill_icon);
                //지도 위의 빅마커 다 삭제
                tMapView.removeAllMarkerItem();
                //지도 위의 마커 다 삭제
                activeMarkerList = tMapView.getAllMarkerItem2();
                for(int i =0;i<activeMarkerList.size();i++){
                    tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                }


                if (worldcupMode) {
                    worldcupMode = false;
                    tMapView.removeAllTMapCircle();
                    worldcupStartButton.setVisibility(View.INVISIBLE);
                    worldcupButton.setBackgroundResource(R.drawable.new_blank_trophy);
                }

                if (likeMode){
                    likeMode = false;
                    worldcupStartButton.setVisibility(View.INVISIBLE);
                    likeButton.setBackgroundResource(R.drawable.ic_playlist_new_1);
                    //유튜버 버튼 레이아웃 fill
                }
            }
        });

        worldcupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldcupMode = true;

                searchButton.setBackgroundResource(R.drawable.new_blank_home);
                likeButton.setBackgroundResource(R.drawable.ic_playlist_new_1);
                worldcupButton.setBackgroundResource(R.drawable.new_fill_trophy);
                worldcupStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        worldcupStart();
                    }
                });
                worldcupStartButton.setText("시작");
                worldcupStartButton.setVisibility(View.VISIBLE);
                setWorldcupCircle();

                if (youtuberMode || likeMode) {
                    youtuberMode = false;
                    likeMode = false;
                    activeMarkerList = tMapView.getAllMarkerItem2();
                    for(int i =0;i<activeMarkerList.size();i++) {
                        tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                    }

                    youtuberButton.setBackgroundResource(R.drawable.youtuber_icon);
                    youtuberListview.setVisibility(View.INVISIBLE);
                    linearLayoutLocationInput.setVisibility(View.VISIBLE);

                    if (bigMode) {
                        showMarker(bigMarkerList, tMapView.getCenterPoint(), bigMode, tMapView);
                    } else {
                        showMarker2(global.getMarkerList(), tMapView.getCenterPoint(), bigMode, tMapView);
                    }

                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeMode = true;

                worldcupStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //List 뜨게 해야함 여기서
                        intent = new Intent(getApplicationContext(), LikeActivity.class);
                        startActivityForResult(intent,100);
                    }
                });
                worldcupStartButton.setText("List");
                worldcupStartButton.setVisibility(View.VISIBLE);
                //위치검색창 invisible
                linearLayoutLocationInput.setVisibility(View.VISIBLE);
                //서치 버튼 레이아웃 blank
                searchButton.setBackgroundResource(R.drawable.new_blank_home);
                //유튜버 버튼 레이아웃 blank
                youtuberButton.setBackgroundResource(R.drawable.youtuber_icon);
                //라이크 버튼 레이아웃 fill
                likeButton.setBackgroundResource(R.drawable.ic_playlist_new_2);
                //지도 위의 빅마커 다 삭제
                youtuberListview.setVisibility(View.INVISIBLE);
                tMapView.removeAllMarkerItem();
                //지도 위의 마커 다 삭제
                activeMarkerList = tMapView.getAllMarkerItem2();
                for(int i =0;i<activeMarkerList.size();i++){
                    tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                }

                if (worldcupMode) {
                    worldcupMode = false;
                    tMapView.removeAllTMapCircle();
                    worldcupButton.setBackgroundResource(R.drawable.new_blank_trophy);
                }

                showLikeMarker(global, tMapView);
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
                if (tMapView.getZoomLevel() == 15 ) {
                    if(!youtuberMode && !likeMode) {
                        parseBigMarker(tMapView.getCenterPoint());
                    }
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
                if (tMapView.getZoomLevel() == 16 ) {
                    if(!youtuberMode && !likeMode) {
                        mergeMarker(tMapView.getCenterPoint());
                    }
                    bigMode = true;
                }
            }
        });

        // 시작
        int end = global.getTargetListArray().size();
            int middle = end/2;

        new Thread(new Runnable() {
            @Override public void run() {
                Log.e("getTargeList 시작 전", "시작 전");
                if (makeBigMarker(global.getTargetListArray(), bigMarkerList)) {    // 마커 생성
                    showMarker(bigMarkerList, centerPoint, bigMode, tMapView);
                    Log.i("Thread1", "makeMarker");
                    if (global.getMarkerList().isEmpty())
                        makeMarker(global.getTargetListArray(), global.getMarkerList());
                    //makeMarker(new ArrayList<TargetList> (global.getTargetListArray().subList(0, middle)), markerList);
                }
            }
        }).start();

    }

    /*public void init(final TMapPoint centerPoint) {

        // 전체 음식점 정보 json으로 받아오기
        searchDB = new SearchDB();
        if (jsonObjectArrayList.isEmpty()) {
            Log.w("init", "jsonObjectArrayList 비어있어서 searchDB.returnData");
            searchDB.returnData(getApplicationContext());
        }
    }*/

    // db에 유튜브 리스트 요청 및 정제 refactorJS

    /*public void getTargetList(ArrayList<JSONObject> jsonObjectArrayList) throws MalformedURLException {

        Gson gson = new Gson();
        YoutubeItem[] temptubeItems;
        String[] tempUrls;
        TargetList target;

        for (JSONObject jsonObject : jsonObjectArrayList) {
            String response = jsonObject.toString();

            target = gson.fromJson(response, TargetList.class);
            target.youtubeItems = new ArrayList<>();
            target.resImageUrlList = new ArrayList<>();

            temptubeItems = gson.fromJson(target.youtube, YoutubeItem[].class);
            tempUrls = gson.fromJson(target.resImageURL, String[].class);

            for (YoutubeItem temptubeItem : temptubeItems) {
                target.youtubeItems.add(temptubeItem);
            }
            //에러 발생 부분
            for (String tempUrl : tempUrls) {
                if ( URLUtil.isValidUrl(tempUrl) )
                    target.resImageUrlList.add(tempUrl);
            }

            global.getTargetListArray().add(target);
        }
    }*/

    public void mergeMarker(TMapPoint centerPoint) {
        Log.d("mergeMarker", "delete Marker & makeBigMarker");

        deleteMarker2(tMapView, global.getMarkerList());
        showMarker(bigMarkerList,centerPoint, bigMode, tMapView);
    }


    public void parseBigMarker(TMapPoint centerPoint) {
        Log.d("parseBigMarker", "delete BigMarker & makeMarker");

        deleteMarker(tMapView, bigMarkerList);
        showMarker2(global.getMarkerList(), centerPoint, bigMode, tMapView);
    }


    public void setWorldcupCircle() {
        tMapView.removeAllTMapCircle();
        tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( tMapView.getCenterPoint() ); // 센터 설정
        tMapCircle.setRadius(1000);
        tMapCircle.setCircleWidth(15);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);
        tMapView.addTMapCircle("circle", tMapCircle);
    }


    public void worldcupStart() {
        intent = new Intent(getApplicationContext(), WorldcupActivity.class);
        intent.putExtra("lat", tMapView.getCenterPoint().getLatitude());
        intent.putExtra("lng", tMapView.getCenterPoint().getLongitude());
        //intent.putExtra("targetList", targetListForIntent);

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

        adapter = new YoutuberAdapter(this, itemList, onClickItem);
        youtuberListview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        youtuberListview.addItemDecoration(decoration);
    }


    private void youtuberFilter(ArrayList<TargetList> targetListArray){

        Map<String,Integer> tempArray = new HashMap<>();

        //Hashmap 생성
        for(TargetList targetList : targetListArray) {
            if (targetList.resAddress.contains(guName)) {
                for (YoutubeItem youtubeItem : targetList.youtubeItems) {
                    if (!(tempArray.containsKey(youtubeItem.channel))) {
                        tempArray.put(youtubeItem.channel, 1);
                    } else {
                        tempArray.put(youtubeItem.channel, tempArray.get(youtubeItem.channel) + 1);
                    }
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
            try {
                Map.Entry entry = (Map.Entry)iterator.next();
                top10YoutuberList[i].channelName = (String)entry.getKey();
                Log.d("sortedHashmap 값 ", (String)entry.getKey());
                Log.d("sortedHashmap 값 ", entry.getValue().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i =0;i<top10YoutuberList.length;i++){
            for( int j =0 ;j<targetListArray.size();j++){
                for (int k =0 ; k<targetListArray.get(j).youtubeItems.size();k++){
                    if(targetListArray.get(j).youtubeItems.get(k).channel.equals(top10YoutuberList[i].channelName)){
                        top10YoutuberList[i].indexList.add(j);
                    }
                }
            }
        }

    }


    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            global = (Global)getApplicationContext();
            //v.setBackgroundResource(R.drawable.radius_background_black);
            activeMarkerList = tMapView.getAllMarkerItem2();

            for(int i =0;i<activeMarkerList.size();i++){
                tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
            }

            YoutuberAdapter.ViewHolder textViewList;
            TextView textView = (TextView) v;
            TextView clickedTextView = (TextView) v;
            String str = (String)textView.getTag().toString();
            int strLength = str.length();
            str = str.substring(1,strLength);
            int position = Integer.parseInt(textView.getTag().toString().substring(0,1));
            int clickedPosition = -1;
            //adapter.setInitial();


            for(int i = 0 ;i<10;i++){
                if(checkClicked[i]==true){
                    clickedPosition = i;
                }
            }

            if(clickedPosition!=-1) {
                textViewList = (YoutuberAdapter.ViewHolder) youtuberListview.findViewHolderForLayoutPosition(clickedPosition);
                try {
                    clickedTextView = textViewList.textview;

                    clickedTextView.setBackgroundResource(R.drawable.radius_background_white);
                    checkClicked[clickedPosition] = false;
                } catch (Exception e) {
                    Log.e("onClickItem", "clickedPositon : "+clickedPosition);

                    e.printStackTrace();
                }
            }

            if(position != clickedPosition) {
                textView.setBackgroundResource(R.drawable.radius_background_orange);
                //textView.setTextColor(getResources().getColor(android.R.color.white));
                checkClicked[position] = true;
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                for(int i=0; i<top10YoutuberList.length; i++){
                    if(top10YoutuberList[i].channelName.equals(str)){
                        showYoutuberMarker(global.getMarkerList(), top10YoutuberList[i], tMapView);
                        break;
                    }
                }
            }

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == 110) {
                Log.d("바부야","바부야");
                activeMarkerList = tMapView.getAllMarkerItem2();
                for(int i =0;i<activeMarkerList.size();i++){
                    tMapView.removeMarkerItem2(activeMarkerList.get(i).getID());
                }
                showLikeMarker(global, tMapView);
            } else {   // RESULT_CANCEL
                Toast.makeText(Search2Activity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
