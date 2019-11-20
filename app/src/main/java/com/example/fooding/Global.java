package com.example.fooding;

import android.app.Application;

import com.example.fooding.Target.TargetList;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

public class Global extends Application {
    private ArrayList<TargetList> targetListArray = new ArrayList<>(100);
    private ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>(100);

    public ArrayList<TargetList> getTargetListArray(){
        return targetListArray;
    }
    public ArrayList<JSONObject> getJsonObjectArrayList() { return jsonObjectArrayList;}
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

}
