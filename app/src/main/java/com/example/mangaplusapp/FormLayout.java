package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mangaplusapp.databinding.ActivityMainBinding;

import java.text.Normalizer;


public class FormLayout extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // BEGIN FORM Validition

//        handle loginBtn click    , start login screen
        findViewById(R.id.loginBtn_form).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(FormLayout.this,LoginActivity.class));
            }
        });
        //handle skipBtn click    , start login screen

//        findViewById(R.id.skipBtn).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                startActivity(new Intent(FormLayout.this,DashBoardUserActivity.class));
//            }
//        });
    }
}