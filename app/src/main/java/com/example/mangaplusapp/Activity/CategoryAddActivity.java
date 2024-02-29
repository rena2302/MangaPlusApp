package com.example.mangaplusapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityCategoryAddBinding;
import com.example.mangaplusapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CategoryAddActivity extends AppCompatActivity {
    private ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        onClickEvent();


    }
    private void onClickEvent(){
        binding.addCategorySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.addCategoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private String category = "";
    private void validateData() {
        /*before adding validate data*/

        //get Data
        category = binding.addCategoryName.getText().toString().trim();
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this,"Please enter category name...!", Toast.LENGTH_LONG).show();
        }
        else {
            addCategoryFireBase();
        }
    }

    private void addCategoryFireBase() {
        long timestamp = System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ID_CATEGORY", ""+timestamp);
        hashMap.put("NAME_CATEGORY", ""+category);
        //hashMap.put("UID_CATEGORY", firebaseAuth.getUid());

        //add to firebase db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //category add success
                        Toast.makeText(CategoryAddActivity.this,"Category add successful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //category add fail
                        Toast.makeText(CategoryAddActivity.this,"Category add failure", Toast.LENGTH_LONG).show();
                    }
                });
    }
}