package com.example.mangaplusapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaplusapp.Helper.DBHelper.MangaDBHelper;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityAddMangaBinding;
import com.example.mangaplusapp.object.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MangaAddActivity extends AppCompatActivity {
    ActivityAddMangaBinding binding;
    private FirebaseAuth firebaseAuth;
    private List<Category> categoryList;
    private String selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMangaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loadCategories();
        onClickEvent();

    }

    private void loadCategories() {
        //get categories from firebase
        categoryList = new ArrayList<>();
        //load fire db > categories --> categoryList
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    //get Data
                    Category category = ds.getValue(Category.class);
                    //add to list
                    categoryList.add(category);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MangaAddActivity.this, "The loading categories was interrupted",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onClickEvent(){
        binding.addMangaSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.addMangaCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });
        binding.addMangaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void categoryPickDialog() {
        //get string Array from categoryList
        String[] categoriesString = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++){
            categoriesString[i] = categoryList.get(i).getNAME_CATEGORY();
        }

        //alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Pick category")
                .setItems(categoriesString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item click
                        //get clicked item from list
                        String category = categoriesString[which];
                        //set category to textView
                        binding.addMangaCategory.setText(category);

                        selectedCategoryId = categoryList.get(which).getID_CATEGORY();
                    }
                })
                .show();
    }

    private String manga = "";
    private void validateData() {
        /*before adding validate data*/

        //get Data
        manga = binding.addMangaName.getText().toString().trim();
        if(TextUtils.isEmpty(manga)){
            Toast.makeText(this,"Please enter manga name...!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(binding.addMangaCategory.getText())) {
            Toast.makeText(this,"Please pick category...!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(binding.addMangaDescription.getText())) {
            Toast.makeText(this,"Please enter manga description..!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(binding.addMangaPicture.getText())) {
            Toast.makeText(this,"Please set manga picture...!", Toast.LENGTH_LONG).show();
        } else {
            addMangaFireBase();
        }
    }

    private void addMangaFireBase() {
        long timestamp = System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("ID_MANGA", ""+timestamp);
        hashMap.put("NAME_MANGA", ""+manga);
        hashMap.put("DESCRIPTION_MANGA", ""+binding.addMangaDescription.getText());
        hashMap.put("PICTURE_MANGA", ""+binding.addMangaPicture.getText());
        hashMap.put("CATEGORY_MANGA", ""+binding.addMangaCategory.getText());
        hashMap.put("ID_CATEGORY_MANGA",""+selectedCategoryId);
        //hashMap.put("UID_MANGA", firebaseAuth.getUid());

        //add to firebase db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MangaAddActivity.this,"Manga add successful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MangaAddActivity.this,"Manga add fail", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
