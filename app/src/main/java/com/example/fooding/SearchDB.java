package com.example.fooding;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.fooding.Target.TargetList;
import com.example.fooding.Youtube.YoutubeItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.skt.Tmap.TMapPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import androidx.annotation.NonNull;


public class SearchDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Gson gson = new Gson();
    public SearchDB() {
    }

    public void returnData(Context context) {
        Global global = ((Global)context);
        db.collection("GangNam")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                double distance = checkDistance(document,centerPoint);
//                                if(distance>10000000) {
//                                    continue;
//                                }
//                                else {
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        String imageURL = document.getData().get("resImageURL").toString();

                                        jsonObject.put("name",document.getData().get("name"));
                                        jsonObject.put("lat",document.getData().get("lat"));
                                        jsonObject.put("lng",document.getData().get("lng"));
                                        jsonObject.put("description",document.getData().get("description"));
                                        jsonObject.put("resAddress",document.getData().get("resAddress"));
                                        jsonObject.put("resImageURL",setImageArray(imageURL));
                                        jsonObject.put("youtube",document.getData().get("youtube"));

                                        Log.w("getTargetList", jsonObject + "");

                                        //jsonObjectArrayList.add(jsonObject);
                                        setGlobalTarget(global,jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                }
                            }
                        } else {
                            Log.e("returnData", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnYoutube(final ArrayList<JSONObject> jsonObjectArrayList, final JSONObject jsonObject , final QueryDocumentSnapshot document){
        db.collection("GangNam").document(document.getId()).collection("youtube")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> videoList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                JSONObject youtubeitem = new JSONObject();
                                try {
                                    youtubeitem.put("thumbnail",document.getData().get("thumbnail"));
                                    youtubeitem.put("channel",document.getData().get("channel"));
                                    youtubeitem.put("description",document.getData().get("description"));
                                    youtubeitem.put("title",document.getData().get("title"));
                                    youtubeitem.put("URL",document.getData().get("URL"));
                                    videoList.add(youtubeitem.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            try {
                                jsonObject.put("name",document.getData().get("name"));
                                jsonObject.put("lat",document.getData().get("lat"));
                                jsonObject.put("lng",document.getData().get("lng"));
                                jsonObject.put("description",document.getData().get("description"));
                                jsonObject.put("resAddress",document.getData().get("resAddress"));
                                jsonObject.put("resImageURL",document.getData().get("resImageURL"));
                                jsonObject.put("youtube",videoList);

                                Log.d("returnYoutube",jsonObject.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("returnYoutube", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public double checkDistance(QueryDocumentSnapshot document, TMapPoint point){
        Location locationA = new Location("point A");
        Location locationB = new Location("point B");

        locationA.setLatitude(point.getLatitude());
        locationA.setLongitude(point.getLongitude());

        locationB.setLatitude(Double.parseDouble(document.getData().get("lat").toString()));
        locationB.setLongitude(Double.parseDouble(document.getData().get("lng").toString()));

        double distance = locationA.distanceTo(locationB);

        return distance;
    }

    public String setImageArray(String str){
        String[] array = str.substring(1,str.length()-1).split(",");
        String imageArray = new String();
        for(int i = 0 ;i<array.length;i++){
            if(i == 0) {
                array[i] = "[\"" + array[i] + "\",";
            }
            else if(i==array.length-1){
                array[i] = "\"" + array[i].substring(1) + "\"]";
            }
            else{
                array[i] = "\"" + array[i].substring(1) + "\",";
            }
            imageArray = imageArray + array[i];
        }
        return imageArray;
    }

    public void setGlobalTarget(Global global,JSONObject jsonObject){

        YoutubeItem[] temptubeItems;
        String[] tempUrls;
        TargetList target;
        String response = jsonObject.toString();

        target = gson.fromJson(response, TargetList.class);
        target.youtubeItems = new ArrayList<>();
        target.resImageUrlList = new ArrayList<>();

        temptubeItems = gson.fromJson(target.youtube, YoutubeItem[].class);
        tempUrls = gson.fromJson(target.resImageURL, String[].class);

        for (int j = 0; j < temptubeItems.length; j++) {
            target.youtubeItems.add(temptubeItems[j]);
        }
        //에러 발생 부분
        for (int j = 0; j < tempUrls.length; j++) {
            if (URLUtil.isValidUrl( tempUrls[j] ) ) {
                target.resImageUrlList.add(tempUrls[j]);
            }
        }
         global.getTargetListArray().add(target);
        }
    }

