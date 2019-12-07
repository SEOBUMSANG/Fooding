package com.example.fooding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private String urlStr;
    private ImageView imageView;

    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();

    public ImageLoadTask(String urlStr, ImageView imageView) {
        this.urlStr = urlStr;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            if (bitmapHash.containsKey(urlStr)) {
                Bitmap oldBitmap = bitmapHash.remove(urlStr);
                if (oldBitmap != null) {
                    oldBitmap.recycle();
                    oldBitmap = null;
                }
            }

            URL url = new URL(urlStr);
            try {
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            bitmapHash.put(urlStr, bitmap);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //mSaveBm = bitmap;
        imageView.setImageBitmap(bitmap);
        imageView.invalidate(); //다시 그리기 (일반적으로 UI 객체는 invalidate를 직접 해줄 필요X)
    }

}
