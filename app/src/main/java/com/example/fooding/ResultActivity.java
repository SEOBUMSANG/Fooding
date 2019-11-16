package com.example.fooding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.fooding.Target.TargetList;
import com.example.fooding.WorldCup.WorldcupItemView;
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

        intent = getIntent();
        result = intent.getParcelableExtra("result");
        WorldcupItemView worldcupResultItem = new WorldcupItemView(getApplicationContext(), result);

        ImageView imageView = (ImageView) findViewById(R.id.trophy_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.trophy_gif).into(gifImage);


        FrameLayout resultLayout = findViewById(R.id.result_layout);

        routeButton = (Button) findViewById(R.id.route_button);
        restartButton = (Button) findViewById(R.id.restart_button);

        resultLayout.addView(worldcupResultItem);

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(),RouteActivity.class);
                intent.putExtra("result", result);
                startActivityForResult(intent, 401);
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
