package com.example.fooding.WorldCup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
import com.example.fooding.Youtube.ResImageUrl;

import java.lang.annotation.Target;
import java.net.MalformedURLException;

public class WorldcupItemView extends LinearLayout {
    Context mcontext;

    ImageView candidateImageViewLeft;
    ImageView candidateImageViewRight;
    TextView candidateTitleView;
    TextView candidateDescriptionView;
    ImageLoadTask imageLoadTask1;
    ImageLoadTask imageLoadTask2;

    public WorldcupItemView(Context context, TargetList candidateTarget, ResImageUrl resImageUrl){
        super(context);

        init(context, candidateTarget);

        if ( !candidateTarget.resImageUrlList.isEmpty() ) {
            imageLoadTask1 = new ImageLoadTask(resImageUrl.resImageUrl1, candidateImageViewLeft);
            imageLoadTask1.execute();
            imageLoadTask2 = new ImageLoadTask(resImageUrl.resImageUrl2, candidateImageViewRight);
            imageLoadTask2.execute();

            /*
            Thread leftImageThread = new Thread() {
                public void run() {
                    imageLoadTask1 = new ImageLoadTask(candidateTarget.resImageUrlList.get(0), candidateImageViewLeft);
                    imageLoadTask1.execute();
                }
            };
            leftImageThread.start();

            Thread rightImageThread = new Thread() {
                public void run() {
                    imageLoadTask2 = new ImageLoadTask(candidateTarget.resImageUrlList.get(1), candidateImageViewRight);
                    imageLoadTask2.execute();
                }
            };
            rightImageThread.start();
            */

        }
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
        candidateDescriptionView = findViewById(R.id.candidate_description_view);

        candidateTitleView.setText(candidateTarget.name);
        candidateDescriptionView.setText(candidateTarget.description);
    }


}
