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
        setTextItem();
        onClickEvent();
        loadChapters();
        setFavorite();
    }

    private void loadChapters() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapterList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Chapters chapter = ds.getValue(Chapters.class);
                    if (chapter != null && chapter.getID_MANGA_CHAPTER().equals(mangaId)) {
                        chapterList.add(chapter);
                    }
                }
                //setup LayoutManager
                binding.mangaDetailRcv.setLayoutManager(new LinearLayoutManager(MangaDetailActivity.this,LinearLayoutManager.VERTICAL, false));
                //setup adapter
                chapterAdapter = new ChapterAdapter();
                chapterAdapter.setData(MangaDetailActivity.this, chapterList);
                //set adapter
                binding.mangaDetailRcv.setAdapter(chapterAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MangaDetailActivity.this, "The loading mangas was interrupted",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onClickEvent(){
        binding.backDetailProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.mangaDetailDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MangaDetailActivity.this);
                builder.setTitle("Manga Description")
                        .setMessage(mangaDescription)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        binding.mangaDetailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
    }
    protected void addToFavorite(){
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"You're not login", Toast.LENGTH_SHORT).show();
            return;
        }else {
            long timestamp = System.currentTimeMillis();
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("ID_MANGA", mangaId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(mangaId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MangaDetailActivity.this, "Add favorite successful", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    protected void removeFromFavorite(String mangaIdToRemove){
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"You're not logged in", Toast.LENGTH_SHORT).show();
            return;
        }else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(mangaIdToRemove)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MangaDetailActivity.this, "Remove from favorite successful", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MangaDetailActivity.this, "Failed to remove from favorite", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MangaDetailActivity.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void toggleFavorite() {
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Favorites");
        userFavoritesRef.child(mangaId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().getKey();
                        if (dataSnapshot.exists()) {
                            // Manga is already in favorites, remove it
                            binding.mangaDetailFavorite.setImageResource(R.drawable.ic_favorite);
                            removeFromFavorite(mangaId);
                        } else {
                            // Manga is not in favorites, add it
                            binding.mangaDetailFavorite.setImageResource(R.drawable.ic_favorite_fill);
                            addToFavorite();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void setFavorite(){
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Favorites");
        userFavoritesRef.child(mangaId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.mangaDetailFavorite.setImageResource(R.drawable.ic_favorite_fill);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void setTextItem() {
        binding.mangaDetailDescription.setText(intent.getStringExtra("DESCRIPTION_MANGA"));
        binding.mangaDetailTitle.setText(intent.getStringExtra("NAME_MANGA"));
        Glide.with(binding.mangaDetailImg)
                .load(intent.getStringExtra("PICTURE_MANGA"))
                .into(binding.mangaDetailImg);
    }
    @Override
    public void onBackPressed() {
        // Xử lý sự kiện khi nút back được nhấn trong activity này
        // Ví dụ: Trở về màn hình trước đó hoặc thoát ứng dụng
        startActivity(new Intent(MangaDetailActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}