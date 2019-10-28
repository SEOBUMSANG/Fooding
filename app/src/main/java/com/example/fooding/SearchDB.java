package com.example.fooding;

import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import androidx.annotation.NonNull;


public class SearchDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SearchDB() {
    }

    public void returnData(final ArrayList<JSONObject> jsonObjectArrayList) {
        db.collection("Crawling")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject jsonObject = new JSONObject();
                                Log.d("TAG", document.getId() + " => " + document.getData().get("name"));
                                returnYoutube(jsonObjectArrayList,jsonObject,document.getId(),document.getData().get("name"),document.getData().get("lat"),document.getData().get("lng"));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnYoutube(final ArrayList<JSONObject> jsonObjectArrayList,final JSONObject jsonObject ,final Object id,final Object name,final Object lat,final Object lng){
        db.collection("Crawling").document(id.toString()).collection("youtube")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> videoList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                videoList.add(document.getData().toString());
                            }
                            try {
                                jsonObject.put("name",name);
                                jsonObject.put("lat",lat);
                                jsonObject.put("lng",lng);
                                jsonObject.put("youtube",videoList);
                                Log.d("tag",jsonObject.toString());
                                jsonObjectArrayList.add(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}