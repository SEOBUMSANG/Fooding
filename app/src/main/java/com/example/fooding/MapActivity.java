package com.example.fooding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooding.Target.TargetList;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;


import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    TMapMarkerItem firstMarkerItem;
    float[] distance = new float[1];


    public boolean makeBigMarker(ArrayList<TargetList> targetList, ArrayList<TMapMarkerItem> bigMarkerList) {

        if (targetList == null || targetList.size() == 0) {
            Log.e("makeBigMarker", "json 어레이 비어있음");
            return false;
        }

        // 첫 번째 빅마커 생성
        if (bigMarkerList.isEmpty()) {
            TargetList firstTarget = targetList.get(0);
            firstMarkerItem = new TMapMarkerItem();
            firstMarkerItem.setID("firstBigMarkerItem");
            bigMarkerList.add(firstMarkerItem);
        }

        TMapMarkerItem markerItem;
        TMapPoint tMapPoint;
        for (int i = 0; i < targetList.size(); i++) {
            // 마커 생성
            markerItem = new TMapMarkerItem();
            tMapPoint = new TMapPoint(Double.parseDouble(targetList.get(i).lat), Double.parseDouble(targetList.get(i).lng));
            markerItem.setTMapPoint( tMapPoint ); // 마커의 좌표 지정
            markerItem.setID(targetList.get(i).name);

            // 거리 계산
            Location.distanceBetween(firstMarkerItem.latitude, firstMarkerItem.longitude, markerItem.latitude, markerItem.longitude, distance);

            if (distance[0] <= 2800) {
                //firstMarkerItem.markerList.add(markerItem);
            } else {
                firstMarkerItem = markerItem;
                //firstMarkerItem.markerList.add(markerItem); // 자기 자신도 추가
                bigMarkerList.add(firstMarkerItem);
            }
        }

        for (TMapMarkerItem bigMarkerItem : bigMarkerList) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_red);
            bigMarkerItem.setIcon(resizeBitmap(bitmap, 150)); // 마커 아이콘 지정
            bigMarkerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        }


        if (bigMarkerList.isEmpty()) {
            Log.e("makeBigMarker", "big 마커 어레이 비어있음");
            return false;
        }

        return true;
    }


    public boolean makeMarker(ArrayList<TargetList> targetList, ArrayList<TMapMarkerItem2> markerList) {
        Global global = ((Global)getApplicationContext());

        if (targetList == null || targetList.size() == 0) {
            Log.e("makeMarker", "어레이 비어있음");
            return false;
        }

        //refactorJS
        TargetList eachTarget;
        for (int i = 0; i < targetList.size(); i++) {
            eachTarget = targetList.get(i);
            Log.i("makeMarker", eachTarget.name);

            // 마커 생성
            MarkerOverlay markerItem = new MarkerOverlay(this, eachTarget);
            markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
            String sID = eachTarget.name;

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_blue);
            markerItem.setIcon(resizeBitmap(bitmap, 200)); // 마커 아이콘 지정
            markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem.setID(sID); // 마커의 id 지정

            markerList.add(markerItem);
        }

        if (markerList.isEmpty()) {
            return false;
        }

        return true;
    }

    public void deleteMarker(TMapView tMapView, ArrayList<TMapMarkerItem> markerList){
        for(int i=0;i<markerList.size();i++) {
            tMapView.removeMarkerItem(markerList.get(i).getID());
        }
        //markerList.clear();
        Log.d("deleteMarker","지도에서 빅마커 삭제");
    }


    public void deleteMarker2(TMapView tMapView, ArrayList<TMapMarkerItem2> markerList){
        for(int i=0;i<markerList.size();i++) {
            tMapView.removeMarkerItem2(markerList.get(i).getID());
        }
        //markerList.clear();
        Log.d("deleteMarker","지도에서 마커 삭제");
    }

    public Bitmap resizeBitmap(Bitmap original, int width) {
        int resizeWidth = width;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if( result != original) {
            original.recycle();
        }
        return result;
    }

}
