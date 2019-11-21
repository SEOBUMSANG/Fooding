package com.example.fooding;

import androidx.annotation.Nullable;

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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.fooding.Target.TargetList;

import com.example.fooding.Youtube.ResImageUrl;
import com.example.fooding.Youtube.YoutubeItem;
import com.google.gson.Gson;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.location.Address;
import org.json.JSONObject;


public class Search2Activity extends MapActivity {
    TMapView tMapView;
    Intent intent;

    Button refreshButton;

    CurrentGps currentGps;
    ArrayList<JSONObject> jsonObjectArrayList;
    ArrayList<TMapMarkerItem> bigMarkerList;
    static ArrayList<TMapMarkerItem2> markerList;
    ArrayList<TMapMarkerItem2> partMarkerList;

    TargetList[] targetList;
    ArrayList<TargetList> targetListForIntent;
    ArrayList<ResImageUrl> resImageUrls;


    SearchDB searchDB;

    boolean bigMode = true;
    boolean worldcupMode = false;
    float[] dist = new float[1];

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
        final EditText locationInput = findViewById(R.id.location_input);
        Button locationInputButton = findViewById(R.id.location_input_button);

        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);
        final Button worldcupStartButton = findViewById(R.id.worldcup_start_button);

        final Button searchButton = findViewById(R.id.search_button);
        final RelativeLayout layoutSearchButton = findViewById(R.id.layout_search_button);
        final Button youtuberButton = findViewById(R.id.youtuber_button);
        final Button worldcupButton = findViewById(R.id.worldcup_button);
        final RelativeLayout layoutWorldcupButton = findViewById(R.id.layout_worldcup_button);
        Button likeButton = findViewById(R.id.like_button);


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
                if (worldcupMode) {
                    worldcupMode = false;
                    tMapView.removeMarkerItem(centerMarkerItem.getID());
                }

                layoutSearchButton.setBackgroundResource(R.drawable.oval_background_orange_fill);
                layoutWorldcupButton.setBackgroundResource(R.drawable.oval_background_orange_blank);
                worldcupStartButton.setVisibility(View.INVISIBLE);
            }
        });

        youtuberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), YoutuberActivity.class);
                intent.putExtra("point", centerPointList);
                intent.putParcelableArrayListExtra("targetListForYoutuberActivity", targetListForIntent);

                startActivityForResult(intent, 203);
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
                    for (int i = 0; i < targetList.length; i++) {
                        targetListForIntent.add(targetList[i]);
//                        if ( !targetListForIntent.get(i).resImageUrlList.isEmpty() )
//                            Log.i("intent용 target으로 옮길 때 ", "" + targetListForIntent.get(i).resImageUrlList.get(0) );
                    }

                    if (makeBigMarker(targetList, bigMarkerList)) {    // 마커 생성
                        showMarker(bigMarkerList, centerPoint);
                        makeMarker(targetList, markerList);
                    }
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

            for (int j = 0; j < 4; j++) {
                if (URLUtil.isValidUrl( tempUrls[j] ) && tempUrls.length == 4 ) {
                    targetList[i].resImageUrlList.add( tempUrls[j] );
                }
                else{
                    targetList[i].resImageUrlList.add( "http://3.bp.blogspot.com/-WhBe10rJzG4/U4W-hvWvRCI/AAAAAAAABxg/RyWcixpgr3k/s1600/noimg.jpg&type=f420_312" );
                }
            }
        }
        setResImageUrl(targetList);
    }

    public void setResImageUrl (TargetList[] targetList){
        ResImageUrl tempResUrl ;

        resImageUrls = new ArrayList<ResImageUrl>();

        for(int i=0; i<targetList.length;i++){
            tempResUrl = new ResImageUrl(targetList[i].name, targetList[i].getResImageUrlList().get(0), targetList[i].getResImageUrlList().get(1));
            resImageUrls.add(tempResUrl);
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
        intent.putExtra("resImageUrlList", resImageUrls);

        startActivityForResult(intent, 203);
    }

    /*public void refreshMarker(){

        if (tMapView.getZoomLevel() >= 15) {
            deleteMarker(tMapView, markerList);
        } else {
            deleteMarker(tMapView, bigMarkerList);
        }

        float[] distance = new float[1];
        TMapPoint centerPoint = tMapView.getCenterPoint();
        if (tMapView.getZoomLevel() >= 15) {
            partMarkerList.clear();
            for (TMapMarkerItem2 markerItem : markerList) {
                Location.distanceBetween(centerPoint.getLatitude(), centerPoint.getLongitude(), markerItem.latitude, markerItem.longitude, distance);

                if (distance[0] <= 2000) {
                    partMarkerList.add(markerItem);

                }
            }
            showMarker(partMarkerList);
        } else {
            partMarkerList.clear();
            for (TMapMarkerItem2 bigMarkerItem : bigMarkerList) {
                Location.distanceBetween(centerPoint.getLatitude(), centerPoint.getLongitude(), bigMarkerItem.latitude, bigMarkerItem.longitude, distance);

                if (distance[0] <= 2000) {
                    partMarkerList.add(bigMarkerItem);
                }
            }
            showMarker(partMarkerList);
        }

    }*/

}
