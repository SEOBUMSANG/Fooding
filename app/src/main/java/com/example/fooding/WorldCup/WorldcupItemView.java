package com.example.fooding.WorldCup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;
import com.example.fooding.Target.TargetList;
import com.example.fooding.Youtube.ResImageUrl;

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
            Log.e("WorldcupItemView", "이 씨발");
            imageLoadTask1 = new ImageLoadTask(candidateTarget.resImageUrlList.get(0), candidateImageViewLeft);
            imageLoadTask1.execute();

            if(candidateTarget.resImageUrlList.size()>=2) {
                imageLoadTask2 = new ImageLoadTask(candidateTarget.resImageUrlList.get(1), candidateImageViewRight);
                imageLoadTask2.execute();
            }
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
