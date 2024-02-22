package com.example.mangaplusapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import Database.MangaDatabase;
import Database.UserDatabase;
import Helper.DBHelper.UserDBHelper;

public class LoadingActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler = new Handler();
        MangaDatabase dbManga = new MangaDatabase(this);
        UserDatabase dbUser = new UserDatabase(this);
        UserDBHelper helper = new UserDBHelper(this);
        dbUser.open();
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