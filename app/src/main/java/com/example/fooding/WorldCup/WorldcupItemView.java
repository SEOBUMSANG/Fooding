package com.example.fooding.WorldCup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;
import com.example.fooding.WorldcupActivity;

public class WorldcupItemView extends LinearLayout {
    Context mcontext;

    Button candidateSelectButton;
    ImageView candidateImageViewLeft;
    ImageView candidateImageViewRight;
    TextView candidateTitleView;

    public WorldcupItemView(Context context) {
        super(context);
        init(context);
    }

    public WorldcupItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

//        if (recommendCallback != null) {
//            recommendCallback = null;
//        }
    }

    private void init(Context context) {
        mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.worldcup_item_view, this, true);

        candidateSelectButton = findViewById(R.id.candidate_select_button);
        candidateImageViewLeft = findViewById(R.id.candidate_image_view_left);
        candidateImageViewRight = findViewById(R.id.candidate_image_view_right);
        candidateTitleView = findViewById(R.id.candidate_title_view);

        candidateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
