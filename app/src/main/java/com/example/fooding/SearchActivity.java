package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skt.Tmap.TMapPoint;

public class SearchActivity extends AppCompatActivity {
    Search2Activity search2Activity = new Search2Activity();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button gangnam = findViewById(R.id.gangnam);
        Button dongjak = findViewById(R.id.dongjak);
        Button seocho = findViewById(R.id.seocho);
        Button guanack = findViewById(R.id.guanack);
        Button songpha = findViewById(R.id.songpha);

        final double[] gangnamPoint = {37.497919, 127.027601};
        final double[] songphaPoint = {37.514513, 127.106080};
        final double[] dongjakPoint = {37.512524, 126.939813};
        final double[] seochoPoint = {37.483573, 127.032667};
        final double[] guanackPoint = {37.478128, 126.951502};

        gangnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch2(gangnamPoint);
            }
        });
        dongjak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch2(dongjakPoint);
            }
        });
        seocho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch2(seochoPoint);
            }
        });
        guanack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch2(guanackPoint);
            }
        });
        songpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch2(songphaPoint);
            }
        });
    }

    public void moveToSearch2(double[] tMapPoint) {
        intent = new Intent(getApplicationContext(), search2Activity.getClass());
        intent.putExtra("point", tMapPoint);
        startActivityForResult(intent, 102);
    }
}
