package com.example.mangaplusapp.Activity.Base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mangaplusapp.R;

public class LoadingActivity extends BaseActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}