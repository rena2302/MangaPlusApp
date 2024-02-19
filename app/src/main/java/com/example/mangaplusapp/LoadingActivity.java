package com.example.mangaplusapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import Database.MangaDatabase;

public class LoadingActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler = new Handler();
        MangaDatabase dbManga = new MangaDatabase(this);
        dbManga.open();

//        dbManga.deleteAllMangaData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}