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
import com.example.fooding.Target.TargetList;
import com.example.fooding.WorldcupActivity;

import java.lang.annotation.Target;

public class WorldcupItemView extends LinearLayout {
    Context mcontext;

    ImageLoadTask imageLoadTask;
    ImageView candidateImageViewLeft;
    ImageView candidateImageViewRight;
    TextView candidateTitleView;

    public WorldcupItemView(Context context, TargetList candidateTarget) {
        super(context);
        init(context, candidateTarget);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

//        if (recommendCallback != null) {
//            recommendCallback = null;
//        }
    }

    private void init(Context context, TargetList candidateTarget) {
        mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.worldcup_item_view, this, true);

        candidateImageViewLeft = findViewById(R.id.candidate_image_view_left);
        candidateImageViewRight = findViewById(R.id.candidate_image_view_right);
        candidateTitleView = findViewById(R.id.candidate_title_view);

        candidateTitleView.setText(candidateTarget.name);
        //imageLoadTask = new ImageLoadTask(candidateTarget.resImageUrlList.get(0).toString(), candidateImageViewLeft);
        //imageLoadTask.execute();
        //imageLoadTask = new ImageLoadTask(candidateTarget.resImageUrlList.get(1).toString(), candidateImageViewRight);
        //imageLoadTask.execute();
    }


}
