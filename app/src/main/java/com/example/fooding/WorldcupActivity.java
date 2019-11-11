package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooding.Target.TargetList;
import com.example.fooding.WorldCup.WorldcupItemView;
import com.google.api.Distribution;

import java.lang.annotation.Target;
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
