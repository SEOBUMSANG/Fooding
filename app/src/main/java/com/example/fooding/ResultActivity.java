package com.example.fooding;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.fooding.Target.TargetList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ResultActivity extends AppCompatActivity {

    private Intent intent;
    private Button routeButton;
    private Button restartButton;
    private Toolbar upToolbar;
    private TargetList result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageView imageView = (ImageView) findViewById(R.id.trophy_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.trophy_gif).into(gifImage);

        intent = getIntent();
        result = intent.getParcelableExtra("result");
        routeButton = (Button) findViewById(R.id.route_button);
        restartButton = (Button) findViewById(R.id.restart_button);

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(),RouteActivity.class);
                intent.putExtra("result",result);
                startActivityForResult(intent, 301);
                finish();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        upToolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar_up);
        upToolbar.bringToFront();
    }

}
