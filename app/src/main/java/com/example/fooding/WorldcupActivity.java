package com.example.fooding;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.fooding.Target.TargetList;
import java.util.ArrayList;

public class WorldcupActivity extends Search2Activity {

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




}
