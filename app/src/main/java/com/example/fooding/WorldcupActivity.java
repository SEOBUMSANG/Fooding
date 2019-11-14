package com.example.fooding;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooding.Target.TargetList;
import java.util.ArrayList;

public class WorldcupActivity extends AppCompatActivity {

    ArrayList<TargetList> candidate;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);

        LinearLayout candidateItemView1 = findViewById(R.id.candidate1_layout);
        LinearLayout candidateItemView2 = findViewById(R.id.candidate1_layout);

        Intent getIntent = getIntent();


    }

    protected void setupView(Context context, final ViewGroup parent, String name, TargetList eachTarget) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.worldcup_item_view, parent, true);

        listView = view.findViewById(R.id.listView);
        title = view.findViewById(R.id.list_title_view);

        // 음식점 이름 설정
        setTitle(name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);

        adapter = new YoutubeAdapter(getContext(), eachTarget.youtubeItems, onClickItem);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        YoutubeItemDecoration decoration = new YoutubeItemDecoration();
        listView.addItemDecoration(decoration);

    }




}
