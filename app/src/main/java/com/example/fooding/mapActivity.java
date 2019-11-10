package com.example.fooding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.util.ArrayList;

public class mapActivity extends AppCompatActivity {
    MarkerOverlay firstMarkerItem;
    float[] distance = new float[1];

    public boolean makeBigMarker(ArrayList<JSONObject> jsonObjectArrayList, ArrayList<TMapMarkerItem2> bigMarkerList) {

        if (jsonObjectArrayList.isEmpty()) {
            Log.e("makeBigMarker", "json 어레이 비어있음");
            return false;
        }

        // 첫 번째 빅마커 생성
        if (bigMarkerList.isEmpty()) {
            JSONObject firstJsonObject = jsonObjectArrayList.get(0);
            String firstResponse = firstJsonObject.toString();
            firstMarkerItem = new MarkerOverlay(this, firstResponse);
            firstMarkerItem.setID("firstBigMarkerItem");
            bigMarkerList.add(firstMarkerItem);
        }

        JSONObject jsonObject;
        String response;
        MarkerOverlay markerItem;
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            response = jsonObject.toString();

            // 마커 생성
            markerItem = new MarkerOverlay(this, response);
            markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
            markerItem.setID("markerItem" + i);


            // 거리 계산
            Location.distanceBetween(firstMarkerItem.latitude, firstMarkerItem.longitude, markerItem.latitude, markerItem.longitude, distance);

            if (distance[0] <= 500) {
                firstMarkerItem.markerList.add(markerItem);
            } else {
                firstMarkerItem = markerItem;
                firstMarkerItem.markerList.add(markerItem); // 자기 자신도 추가
                bigMarkerList.add(firstMarkerItem);
            }
        }

        for (TMapMarkerItem2 bigMarkerItem : bigMarkerList) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_red);
            bigMarkerItem.setIcon(resizeBitmap(bitmap, 100)); // 마커 아이콘 지정
            bigMarkerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        }


        if (bigMarkerList.isEmpty()) {
            Log.e("makeBigMarker", "big 마커 어레이 비어있음");
            return false;
        }

        return true;
    }

    public boolean makeMarker(ArrayList<JSONObject> jsonObjectArrayList, ArrayList<TMapMarkerItem2> markerList) {

        if (jsonObjectArrayList.isEmpty())
            Log.e("makeMarker", "어레이 비어있음");

        JSONObject jsonObject;
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            String response = jsonObject.toString();
            Log.i("makeMarker", response);

            // 마커 생성
            MarkerOverlay markerItem = new MarkerOverlay(this, response);
            markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
            String sID = "markerItem" + i;

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

    public void deleteMarker(TMapView tMapView, ArrayList<TMapMarkerItem2> markerList){

        for(int i=0;i<markerList.size();i++) {
            tMapView.removeMarkerItem2(markerList.get(i).getID());
        }
        //markerList.clear();
        Log.d("deleteMarker","지도에서 (빅)마커 삭제");
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
