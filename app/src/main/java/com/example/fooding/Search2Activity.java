package com.example.fooding;

import androidx.annotation.Nullable;

import android.content.Intent;
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
import android.widget.Toast;
import com.example.fooding.Target.TargetList;

import com.example.fooding.Youtube.YoutubeItem;
import com.google.gson.Gson;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;
import android.location.Address;
import org.json.JSONObject;


public class Search2Activity extends MapActivity {
    TMapView tMapView;
    Intent intent;

    Button youtuberButton;
    Button refreshButton;

    CurrentGps currentGps;
    ArrayList<JSONObject> jsonObjectArrayList;
    ArrayList<TMapMarkerItem2> bigMarkerList;
    ArrayList<TMapMarkerItem2> markerList;

    ArrayList<TMapMarkerItem2> partMarkerList;

    TargetList[] targetList;

    boolean bigMode = true;
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

        // 전체 음식점 정보 json으로 받아오기
        SearchDB searchDB = new SearchDB();
        searchDB.returnData(jsonObjectArrayList, centerPoint);


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
                if(bigMode) {
                    deleteMarker(tMapView, bigMarkerList);
                    showMarker(bigMarkerList, centerPoint);
                }
                else{
                    deleteMarker(tMapView, markerList);
                    showMarker(markerList,centerPoint);
                }    }
        });
        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String s, TMapMarkerItem2 tMapMarkerItem2) {
                Toast.makeText(getApplicationContext(), "marker", Toast.LENGTH_SHORT).show();
                MarkerOverlay marker = (MarkerOverlay) tMapMarkerItem2;

                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.balloonView.youtubeItems[0].URL));
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

        youtuberButton = findViewById(R.id.youtuber_button);
        refreshButton = findViewById(R.id.refresh_button);
        Button worldcupButton = findViewById(R.id.worldcup_button);
        Button likeButton = findViewById(R.id.like_button);

        //갱신 버튼
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

        //아래 toolbar 버튼 설정
        youtuberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), YoutuberActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("point", centerPointList);
                //intent.putParcelableArrayListExtra("jsonArray", jsonObjectArrayList);
                startActivityForResult(intent, 202);
            }
        });
        worldcupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), WorldcupActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("point", centerPointList);
                startActivityForResult(intent, 202);
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


        // 잠시 시간 필요함
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("getTargeList 시작 전", "시작 전");
                getTargetList(jsonObjectArrayList);
                Log.e("getTargeList 완료", "완료");

                if ( makeBigMarker(targetList, bigMarkerList) ) {    // 마커 생성
                    showMarker(bigMarkerList,centerPoint);
                    makeMarker(targetList, markerList);
                }
            }
        }, 5000);

    }

    // db에 유튜브 리스트 요청 및 정제 refactorJS
    public void getTargetList(ArrayList<JSONObject> jsonObjectArrayList){
        Log.e("getTargetList", "시작");
        Gson gson = new Gson();
        JSONObject jsonObject;

        Log.e("getTaretList", "array size : "+jsonObjectArrayList.size());

        YoutubeItem[] temptubeItems;
        targetList = new TargetList[jsonObjectArrayList.size()];
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            String response = jsonObject.toString();

            targetList[i] = gson.fromJson(response, TargetList.class);
            temptubeItems = gson.fromJson(targetList[i].youtube, YoutubeItem[].class);

            for (int j = 0; j < temptubeItems.length; j++) {
                targetList[i].youtubeItems.add(temptubeItems[j]);
            }
            Log.e("getWTargetList", "z"+targetList[i].youtubeItems);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void mergeMarker(TMapPoint centerPoint) {
        Log.d("mergeMarker", "delete Marker & makeBigMarker");

        deleteMarker(tMapView, markerList);

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
            showMarker(markerList,centerPoint);
//        } else {
//            showMarker(partMarkerList);
//        }

    }

    public void showMarker(ArrayList<TMapMarkerItem2> markerList,TMapPoint centerPoint) {
        TMapMarkerItem2 marker = null;
        for (int i = 0; i < markerList.size(); i++) {
            Location.distanceBetween(markerList.get(i).latitude,markerList.get(i).longitude,centerPoint.getLatitude(),centerPoint.getLongitude(),dist);
            if(dist[0]>500){
                continue;
            }
            marker = markerList.get(i);
            tMapView.addMarkerItem2(marker.getID(), marker);    // 지도에 추가
        }
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
