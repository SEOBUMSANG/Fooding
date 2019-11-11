package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.TextView;

import com.skt.Tmap.TMapPoint;

public class WorldcupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);

        TextView candidate1Title = findViewById(R.id.candidate1_title);
        Intent intent = getIntent();


        Log.d("타겟리스트사이즈","" + intent.getParcelableArrayExtra("targetList").length +"");

    }




}
