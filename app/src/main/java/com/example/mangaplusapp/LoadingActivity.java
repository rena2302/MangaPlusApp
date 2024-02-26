package com.example.mangaplusapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import Database.CategoryDatabase;
import Database.MangaDatabase;
import Database.UserDatabase;
import Helper.DBHelper.MangaDBHelper;
import Helper.DBHelper.UserDBHelper;

public class LoadingActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler = new Handler();
        MangaDatabase dbManga = new MangaDatabase(this);
        MangaDBHelper dbMangaHelper = new MangaDBHelper(this);
        UserDatabase dbUser = new UserDatabase(this);
        UserDBHelper helper = new UserDBHelper(this);
        CategoryDatabase dbCate = new CategoryDatabase(this);
        dbCate.open();
        dbUser.open();
        dbManga.open();
//        dbMangaHelper.deleteAllMangaData();
        dbUser.close();
        dbManga.close();
        helper.close();
        dbMangaHelper.close();
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