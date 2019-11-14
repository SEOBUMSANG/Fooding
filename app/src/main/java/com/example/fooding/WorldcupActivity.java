package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooding.Target.TargetList;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.Random;

public class WorldcupActivity extends AppCompatActivity {

    Random rand;
    ArrayList<TargetList> worldcupItem;
    ArrayList<TargetList> worldcupRandomItem;
    ArrayList<TargetList> test;
    Intent intent;
    ArrayList<TargetList> targetListArray;
    Parcelable[] parcelableArray;

    double centerLat;
    double centerLng;
    float[] dist = new float[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);

        LinearLayout candidateLayout1 = findViewById(R.id.candidate1_layout);
        LinearLayout candidateLayout2 = findViewById(R.id.candidate2_layout);
        intent = getIntent();
        centerLat = intent.getExtras().getDouble("lat");
        centerLng = intent.getExtras().getDouble("lng");
        parcelableArray = intent.getParcelableArrayExtra("targetList");
        test = intent.getParcelableArrayListExtra("targetList");
        targetListArray = intent.getParcelableArrayListExtra("targetList");
        Log.d("타겟리스트사이즈","" + targetListArray.get(0) +"");
        rand = new Random();
        Log.d("랜덤",rand.nextInt(100)+" "+rand.nextInt(100));

        worldcupItem = new ArrayList<>();
        worldcupRandomItem = new ArrayList<>();

        setUpWorldCupItem();
        setUpWorldCupRandomItem();
        for(int i = 0 ;i<worldcupRandomItem.size();i++) {
            Log.d("헤헤",worldcupRandomItem.get(i).name+"");
        }

    }


    public void setUpWorldCupItem() {
        Log.d("사이즈","" + targetListArray +"");
        Log.d("쏴아","" + targetListArray.get(0).getName() +"");
        for(int i = 0 ; i < targetListArray.size() ; i++){
            Location.distanceBetween(Double.parseDouble(targetListArray.get(i).getLat()),Double.parseDouble(targetListArray.get(i).getLng()),centerLat,centerLng,dist);
            if(dist[0]>3000){
                Log.d("거리체크1","" + dist[0] +"");
                continue;
            }
            Log.d("거리체크2","" + dist[0] +"");
            worldcupItem.add(targetListArray.get(i));
        }
    }

    public void setUpWorldCupRandomItem() {
        boolean[] checkDup = new boolean[worldcupItem.size()];
        int temp;
        while(worldcupRandomItem.size()<8&&worldcupItem.size()>worldcupRandomItem.size()) {
            temp = rand.nextInt(worldcupItem.size());
            Log.d("하하",temp + "");
            if (checkDup[temp] == false) {
                checkDup[temp] = true;
                worldcupRandomItem.add(worldcupItem.get(temp));
            }
        }

    }
}
