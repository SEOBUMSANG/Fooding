package com.example.fooding;

import android.app.Application;
import com.example.fooding.Target.TargetList;
import com.skt.Tmap.TMapMarkerItem2;
import org.json.JSONObject;
import java.util.ArrayList;


public class Global extends Application {
    private ArrayList<TargetList> targetListArray = new ArrayList<>(100);
    private ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>(100);
    private ArrayList<TMapMarkerItem2> markerList = new ArrayList<>(100);
    private ArrayList<String> likeList = new ArrayList<>(100);

    public Global (){
    }


    public ArrayList<TargetList> getTargetListArray(){
        return targetListArray;
    }

    public ArrayList<JSONObject> getJsonObjectArrayList() { return jsonObjectArrayList;}

    public ArrayList<TMapMarkerItem2> getMarkerList() {return markerList;}

    public ArrayList<String> getlikeList() {return likeList;}


    public void setTargetListArray(ArrayList<TargetList> targetA){
        for(int i = 0;i<targetA.size();i++){
            targetListArray.add(targetA.get(i));
        }
    }

    public void setTargetListArray(TargetList[] targetB){
        for(int i = 0;i<targetB.length;i++){
            targetListArray.add(targetB[i]);
        }
    }

    public void setTargetListItem(TargetList target){
        targetListArray.add(target);
    }

    public void setJsonObjectArrayListItem(JSONObject jsonObject){
        jsonObjectArrayList.add(jsonObject);
    }

    public void setMarkerList(ArrayList<TMapMarkerItem2> markerA){
        for(int i = 0;i<markerA.size();i++){
            markerList.add(markerA.get(i));
        }
    }

    public void setLikeList(String likeItem){
        likeList.add(likeItem);
    }

    public void setMarkerListItem(MarkerOverlay marker){
        markerList.add(marker);
    }
}
