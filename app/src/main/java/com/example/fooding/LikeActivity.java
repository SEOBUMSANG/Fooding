package com.example.fooding;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ListView;
        import androidx.appcompat.app.AppCompatActivity;


public class LikeActivity extends AppCompatActivity {
    ListView listView;
    CommentAdapter adapter;
    Global global;
    Button dButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        Intent intent = getIntent();

        global= ((Global)getApplicationContext());
        adapter = new CommentAdapter(getApplicationContext());
        setCommentList();
        listView = findViewById(R.id.likeListView);
        listView.setAdapter(adapter);




    }

    public void setCommentList() {
        MarkerOverlay marker;
        for(int i=0; i<global.getMarkerList().size(); i++){
            for(int j=0; j<global.getlikeList().size(); j++){
                if(global.getMarkerList().get(i).getID().equals(global.getlikeList().get(j))) {
                    marker = (MarkerOverlay) global.getMarkerList().get(i);
                    adapter.addItem(marker.target);    // 어댑터에 추가
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = new Intent();
        setResult(110, intent2);
        finish();
    }

}
