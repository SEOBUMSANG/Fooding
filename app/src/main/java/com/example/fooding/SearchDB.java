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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class SearchDB {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SearchDB() throws JSONException {
    }

    public void returnData(final ArrayList<JSONObject> jsonObjectArrayList) {
        db.collection("gangnam")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            JSONObject jsonObject = new JSONObject();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData().get("name"));
                                returnYoutube(jsonObjectArrayList,jsonObject,document.getId(),document.getData().get("name"));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnYoutube(final ArrayList<JSONObject> jsonObjectArrayList,final JSONObject jsonObject ,final Object id,final Object name){
        db.collection("gangnam").document(id.toString()).collection("youtube")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            JSONObject videoObject = new JSONObject();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    videoObject.put(document.getId(),document.getData());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                jsonObject.put("name",name);
                                jsonObject.put("youtube",videoObject.toString());
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
