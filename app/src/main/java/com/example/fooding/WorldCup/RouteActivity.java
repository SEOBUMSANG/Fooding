package com.example.fooding.WorldCup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
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
import android.widget.Toast;

import com.example.fooding.MapActivity;
import com.example.fooding.MarkerOverlay;
import com.example.fooding.R;
import com.example.fooding.Target.TargetList;
import com.example.fooding.Youtube.YoutubeItem;
import com.google.gson.Gson;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


public class RouteActivity extends MapActivity {
    private TMapView tMapView;
    private Intent intent;
    private TargetList result;
    private TMapPoint endPoint;
    private TMapPoint centerPoint;
    private Button buttonZoomIn;
    private Button buttonZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        tMapView = new TMapView(this);
        tMapView.setHttpsMode(true);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        tMapView.setCenterPoint( 126.956875,37.504198,  true);
        centerPoint = new TMapPoint(37.504198,126.956875);
        layoutTmap.addView( tMapView );
        Button buttonZoomIn = findViewById(R.id.button_zoom_in);
        Button buttonZoomOut = findViewById(R.id.button_zoom_out);
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
        intent = getIntent();
        result = intent.getParcelableExtra("result");
        endPoint = new TMapPoint(Double.parseDouble(result.getLat()),Double.parseDouble(result.getLng()));

        Log.i("makeMarker", result.name);

        // 마커 생성
        MarkerOverlay markerItem = new MarkerOverlay(this, result);
        markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
        markerItem.setID("result");
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_blue);
        markerItem.setIcon(resizeBitmap(bitmap, 200)); // 마커 아이콘 지정
        markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정

        tMapView.addMarkerItem2(markerItem.getID(), markerItem);

        getCarPath(centerPoint, endPoint);

    }

    public void getWalkPath(TMapPoint startPoint,TMapPoint endPoint){
        TMapData tMapData = new TMapData();
        tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });
    }

    public void getCarPath(TMapPoint startPoint,TMapPoint endPoint){
        TMapData tMapData = new TMapData();
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });
    }
}