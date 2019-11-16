package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.fooding.Target.TargetList;
import com.example.fooding.WorldCup.WorldcupItemView;
import com.google.api.Distribution;
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

    ArrayList <WorldcupItemView> candidatesList;

    FrameLayout.LayoutParams params;
    LinearLayout candidate1;
    LinearLayout candidate2;
    FrameLayout candidateLayout1;
    FrameLayout candidateLayout2;
    Button candidate1_select_button;
    Button candidate2_select_button;

    Context context;
    int index;

    int candidateListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);
        candidateLayout1 = findViewById(R.id.candidate1_layout);
        candidateLayout2 = findViewById(R.id.candidate2_layout);
        candidate1_select_button = findViewById(R.id.candidate1_select_button);
        candidate2_select_button = findViewById(R.id.candidate2_select_button);

        intent = getIntent();
        centerLat = intent.getExtras().getDouble("lat");
        centerLng = intent.getExtras().getDouble("lng");
        targetListArray = intent.getParcelableArrayListExtra("targetList");
        //Log.d("타겟리스트사이즈","" + targetListArray.size() +"");
        rand = new Random();
        Log.d("랜덤",rand.nextInt(100)+" "+rand.nextInt(100));

        worldcupItem = new ArrayList<>();
        worldcupRandomItem = new ArrayList<>();
        candidatesList = new ArrayList<>();

        // 후보로 뽑을 음식점 리스트 세팅
        setUpWorldCupItem();
        setUpWorldCupRandomItem();

        context = getApplicationContext();
        index = 0;

        // layout parameter 세팅
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        if (!worldcupRandomItem.isEmpty()) {
            candidateListSize = worldcupRandomItem.size();
            // 후보들 만들어두기
            for (TargetList target : worldcupRandomItem) {
                candidatesList.add(new WorldcupItemView(context, target));
            }

            // 첫 후보 둘 띄우기
            candidateLayout1.addView(candidatesList.get(index++), params);
            candidateLayout2.addView(candidatesList.get(index++), params);
        }

        // 후보 선택 시 다음
        candidate1_select_button.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View view) {
                if(index != 0) {
                    candidatesList.remove(index-1);
                    worldcupRandomItem.remove(index-1);
                    index -= 1;
                    clickSelectButtonEvent();
                }
                else{
                    //Worldcup 결과창
                }
            }
        }) ;
        candidate2_select_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index != 0) {
                    candidatesList.remove(index-2);
                    worldcupRandomItem.remove(index-2);
                    index -= 1;
                    clickSelectButtonEvent();
                }
                else{
                    //Worldcup 결과창
                }
            }
        }) ;
    }


    public boolean checkStep() {
        Log.v("checkStep", "candidatesList.size() == " + candidatesList.size());
        if (candidatesList.size() == 1) {
            //결과창으로 넘어가기
            return true;
        } else {
            if (candidatesList.size() <= candidateListSize/2) {
                candidateListSize = candidatesList.size();
                index = 0;
            }
            return false;
        }
    }

    public void clickSelectButtonEvent(){
        if (!checkStep()) {
            candidateLayout1.removeAllViewsInLayout();
            candidateLayout2.removeAllViewsInLayout();
            candidateLayout1.addView(candidatesList.get(index++), params);
            candidateLayout2.addView(candidatesList.get(index++), params);
        } else {
            Log.v("checkStep", "최종 하나 선택됨");
            intent = new Intent(getApplicationContext(), WorldcupActivity.class);
            intent.putExtra("result", worldcupRandomItem.get(0));
            startActivityForResult(intent, 301);
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
        while(worldcupRandomItem.size()<8 && worldcupItem.size()>worldcupRandomItem.size()) {
            temp = rand.nextInt(worldcupItem.size());
            Log.d("하하",temp + "");
            if (checkDup[temp] == false) {
                checkDup[temp] = true;
                worldcupRandomItem.add(worldcupItem.get(temp));
            }
        }

    }

}
