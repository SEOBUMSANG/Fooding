package com.example.fooding.Like;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fooding.ImageLoadTask;
import com.example.fooding.R;


public class CommentItemView extends LinearLayout {
    int reviewId;
    ImageView writerImageView;
    ImageLoadTask imageLoadTask;
    TextView nameView;
    TextView descriptionView;
    TextView addressView;
    Button button;

    public CommentItemView(Context context) {
        super(context);
        init(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item_view, this, true);

        writerImageView = (ImageView) findViewById(R.id.writer_image_view);
        nameView = (TextView) findViewById(R.id.name_view);
        descriptionView = (TextView) findViewById(R.id.description_view);
        addressView = findViewById(R.id.resAddress_view);
        button = (Button)findViewById(R.id.button_delete);
    }


    public void setItemImage(String url) {imageLoadTask = new ImageLoadTask(url, writerImageView); imageLoadTask.execute();}
    public void setItemName(String name) {nameView.setText(name);}
    public void setItemDescription(String description) {descriptionView.setText(description);}
    public void setItemAddress(String address) {addressView.setText(address);}

}

