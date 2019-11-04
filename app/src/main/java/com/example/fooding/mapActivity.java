package com.example.fooding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapView;

import org.json.JSONObject;

import java.util.ArrayList;

public class mapActivity extends AppCompatActivity {

    public boolean makeMarker(ArrayList<JSONObject> jsonObjectArrayList, ArrayList<TMapMarkerItem2> markerList) {

        if (jsonObjectArrayList.isEmpty())
            Log.e("TAG", "어레이 비어있음");

        JSONObject jsonObject;
        for (int i = 0; i < jsonObjectArrayList.size(); i++) {
            jsonObject = jsonObjectArrayList.get(i);
            String response = jsonObject.toString();
            Log.i("TAG", response);

            // 마커 생성
            MarkerOverlay markerItem = new MarkerOverlay(this, response);
            markerItem.setTMapPoint( markerItem.markerPoint ); // 마커의 좌표 지정
            String sID = "markerItem" + i;

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_icon_blue);
            markerItem.setIcon(resizeBitmap(bitmap)); // 마커 아이콘 지정
            markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem.setID(sID); // 마커의 id 지정

            markerList.add(markerItem);
        }

        if (markerList.isEmpty()) {
            return false;
        }
        return true;
    }

    public void deleteMarker(TMapView tMapView, ArrayList<JSONObject> jsonObjectArrayList, ArrayList<TMapMarkerItem2> markerList){
        jsonObjectArrayList.clear();
        for(int i=0;i<markerList.size();i++) {
            tMapView.removeMarkerItem2(markerList.get(i).getID());
        }
        markerList.clear();
        Log.d("deleteMarker","마커 삭제");
    }



    public Bitmap resizeBitmap(Bitmap original) {
        int resizeWidth = 200;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if( result != original) {
            original.recycle();
        }
        return result;
    }

}
