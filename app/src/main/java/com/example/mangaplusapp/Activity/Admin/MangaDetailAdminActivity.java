package com.example.mangaplusapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Adapter.ChapterAdapter;
import com.example.mangaplusapp.Adapter.DashBoardAdapter;
import com.example.mangaplusapp.databinding.ActivityMangaDetailAdminBinding;
import com.example.mangaplusapp.databinding.ActivityMangaDetailBinding;
import com.example.mangaplusapp.object.Chapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailAdminActivity extends AppCompatActivity {
    ActivityMangaDetailAdminBinding binding;
    private List<Chapter> chapterList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaDetailAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTextItem();
        loadChapters();
    }

    private void loadChapters() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapterList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Chapter chapter = ds.getValue(Chapter.class);
                    if (chapter != null && chapter.getID_MANGA_CHAPTER().equals(getIntent().getExtras().getString("ID_MANGA"))) {
                        chapterList.add(chapter);
                    }
                }
                //setup LayoutManager
                binding.mangaDetailRcv.setLayoutManager(new LinearLayoutManager(MangaDetailAdminActivity.this,LinearLayoutManager.VERTICAL, false));
                //setup adapter
                DashBoardAdapter  dashBoardAdapter = new DashBoardAdapter(MangaDetailAdminActivity.this,chapterList);
                //set adapter
                binding.mangaDetailRcv.setAdapter(dashBoardAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MangaDetailAdminActivity.this, "The loading mangas was interrupted",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setTextItem() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        binding.mangaDetailDescription.setText(extras.getString("DESCRIPTION_MANGA"));
        binding.mangaDetailTitle.setText(extras.getString("NAME_MANGA"));
        Glide.with(binding.mangaDetailImg)
                .load(extras.getString("PICTURE_MANGA"))
                .into(binding.mangaDetailImg);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MangaDetailAdminActivity.this, DashBoardAdminActivity.class));
        finish();
    }
}