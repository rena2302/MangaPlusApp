package com.example.mangaplusapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaplusapp.Helper.DBHelper.MangaDBHelper;
import com.example.mangaplusapp.R;

public class TestAddMangaActivity extends AppCompatActivity {
    EditText userAddNameTxt,LinkOnlineMangaTxt;
    ImageView getUserAddPictureTxt;
    MangaDBHelper db;
    Button userSubmitBtn,btnSelectImg;
    private static final int SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add_manga);
        db = new MangaDBHelper(this);
        userAddNameTxt = findViewById(R.id.nameMangaTxt);
        getUserAddPictureTxt = findViewById(R.id.pictureMangaSelect);
        userSubmitBtn = findViewById(R.id.mangaAddBtn);
        btnSelectImg = findViewById(R.id.chooseIMGbtn);
        LinkOnlineMangaTxt= findViewById(R.id.LinkOnlineMangaTxt);
        userSubmitBtn.setOnClickListener(v->{
            String name = userAddNameTxt.getText().toString();
            String Link = LinkOnlineMangaTxt.getText().toString();
            db.insertData(name,Link);
            Log.d("ADD LINK ", "TRUE" + Link);
        });
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
        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null){
                String name = userAddNameTxt.getText().toString();
                Uri uri = data.getData();
                // Hiển thị hình ảnh được chọn
                getUserAddPictureTxt.setImageURI(uri);
                // Thêm dữ liệu vào cơ sở dữ liệu
                userSubmitBtn.setOnClickListener(view -> {
                    db.insertData(name, uri);
                    Log.d("ADD URI ", "TRUE" + uri.toString());
                });
        }
    }
}
