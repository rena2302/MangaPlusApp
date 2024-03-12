package com.example.mangaplusapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Adapter.ChapterAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityMangaDetailBinding;
import com.example.mangaplusapp.object.Chapters;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MangaDetailActivity extends BaseActivity {
    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ActivityMangaDetailBinding binding;
    private List<Chapters> chapterList = new ArrayList<>();
    // Khởi tạo adapter trước khi hiển thi
    private ChapterAdapter chapterAdapter ;
    String mangaId, nameManga, mangaPicture, mangaDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        intent = getIntent();
        mangaId = intent.getStringExtra("ID_MANGA");
        nameManga = intent.getStringExtra("NAME_MANGA");
        mangaPicture = intent.getStringExtra("PICTURE_MANGA");
        mangaDescription = intent.getStringExtra("DESCRIPTION_MANGA");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        setContentView(binding.getRoot());
    }


}