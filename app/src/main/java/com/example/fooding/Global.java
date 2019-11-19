package com.example.fooding;

import android.app.Application;

import com.example.fooding.Target.TargetList;

import java.util.ArrayList;

public class Global extends Application {
    private ArrayList<TargetList> targetListArray = new ArrayList<>();

    public ArrayList<TargetList> getState(){
        return targetListArray;
    }
    public void setState(ArrayList<TargetList> targetA){
        for(int i = 0;i<targetA.size();i++){
            targetListArray.add(targetA.get(i));
        }
    }

    public void setState(TargetList[] targetB){
        for(int i = 0;i<targetB.length;i++){
            targetListArray.add(targetB[i]);
        }
    }

}
