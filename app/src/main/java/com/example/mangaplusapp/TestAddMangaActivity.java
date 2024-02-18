package com.example.mangaplusapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import object.TestDBTruyen;

public class TestAddMangaActivity extends AppCompatActivity {
    EditText userAddNameTxt;
    ImageView getUserAddPictureTxt;
    TestDBTruyen db;
    Button userSubmitBtn,btnSelectImg;
    private static final int SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add_manga);
        db = new TestDBTruyen(this);
        userAddNameTxt = findViewById(R.id.nameMangaTxt);
        getUserAddPictureTxt = findViewById(R.id.pictureMangaSelect);
        userSubmitBtn = findViewById(R.id.mangaAddBtn);
        btnSelectImg = findViewById(R.id.chooseIMGbtn);
            btnSelectImg.setOnClickListener(v->{
                Intent intent = new Intent (Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent,"Select Image"),
                        SELECT_IMAGE
                );
            });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_IMAGE && null != data){
            String name = userAddNameTxt.getText().toString();
            Uri uri = data.getData();
            getUserAddPictureTxt.setImageURI(uri);
                userSubmitBtn.setOnClickListener(v->{
                    db.insertData(name,uri);
                    Log.d("ADD URI ", "TRUE");
                });
        }
    }
}
