package com.example.fooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SearchActivity extends AppCompatActivity {
    Intent intent;
    String guName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button gangnam = findViewById(R.id.gangnam);
        Button mapho = findViewById(R.id.mapho);
        Button jongro = findViewById(R.id.jongro);

        final double[] gangnamPoint = {37.497919, 127.027601};
        final double[] jongroPoint = {37.573075, 126.979189};
        final double[] maphoPoint = {37.551588, 126.924975};

        gangnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guName = "강남구";
                moveToSearch2(gangnamPoint);

            }
        });
        mapho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guName = "마포구";
                moveToSearch2(maphoPoint);
            }
        });
        jongro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guName = "종로구";
                moveToSearch2(jongroPoint);
            }
        });
    }

    public void moveToSearch2(double[] tMapPoint) {
        Log.d("왜 안나옴?", guName);
        intent = new Intent(getApplicationContext(), Search2Activity.class);
        intent.putExtra("point", tMapPoint);
        intent.putExtra("guName", guName);
        startActivityForResult(intent, 102);
    }
}
