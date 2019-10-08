package com.example.fooding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class SearchActivity extends AppCompatActivity {
    RelativeLayout layoutSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LinearLayout layoutTmap = findViewById(R.id.layout_tmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("80e66504-97df-4d02-bc81-57c796cd67a1");   //API key setting
        layoutTmap.addView( tMapView );
        tMapView.setCenterPoint(126.963540, 37.509354, true);

        TMapPoint tMapPoint = new TMapPoint(37.509354, 126.963540);
        TMapCircle tMapCircle = new TMapCircle();
        tMapCircle.setCenterPoint( tMapPoint );
        tMapCircle.setRadius(30);
        tMapCircle.setCircleWidth(15);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);
        tMapView.addTMapCircle("circle1", tMapCircle);

        //화면 설정
        Button settingButton = findViewById(R.id.setting_button);
        Button mylocationButton = findViewById(R.id.my_location_button);
        EditText locationInput = findViewById(R.id.location_input);
        Button locationInputButton = findViewById(R.id.location_input_button);

        layoutSearchButton = findViewById(R.id.layout_search_button);
        Button searchButton = findViewById(R.id.search_button);
        Button youtuberButton = findViewById(R.id.youtuber_button);
        Button worldcupButton = findViewById(R.id.worldcup_button);
        Button likeButton = findViewById(R.id.like_button);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
    }
}
