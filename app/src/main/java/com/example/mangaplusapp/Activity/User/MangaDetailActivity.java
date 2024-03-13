package com.example.mangaplusapp.Activity.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
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
    private Boolean mangaPremium;
    public interface OnPurchasedMangaIdsLoadedListener {
        void onPurchasedMangaIdsLoaded(Boolean premium);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        intent = getIntent();
        mangaPremium = Boolean.parseBoolean(intent.getStringExtra("PREMIUM_MANGA"));
        setContentView(binding.getRoot());
        mangaId = intent.getStringExtra("ID_MANGA");
        nameManga = intent.getStringExtra("NAME_MANGA");
        mangaPicture = intent.getStringExtra("PICTURE_MANGA");
        mangaDescription = intent.getStringExtra("DESCRIPTION_MANGA");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        loadPurchasedMangaIds(new OnPurchasedMangaIdsLoadedListener() {
            @Override
            public void onPurchasedMangaIdsLoaded(Boolean premium) {

                onClickPayment(premium);

            }
        });
        onClickEvent();
        setFavorite();
        setTextItem();
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
    private void onClickPayment(Boolean isPremium){

        binding.BuyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPremium) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MangaDetailActivity.this);
                    builder.setTitle("Premium Manga")
                            .setMessage("If you wanna read this manga, you have to buy it")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startNewActivityAndFinishCurrent(PaymentActivity.class,
                                            "ID_MANGA", mangaId);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }else {
                    DatabaseReference chapterRef = FirebaseDatabase.getInstance().getReference("Chapters");
                    chapterRef.orderByChild("ID_MANGA_CHAPTER").equalTo(mangaId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Chapters chapter = dataSnapshot.getValue(Chapters.class);
                                        if (chapter.getNAME_CHAPTER().equals("Chapter 1")) {
                                            startNewActivityAndFinishCurrent(ChapterPdfActivity.class,
                                                    "ID_CHAPTER", chapter.getID_CHAPTER(),
                                                    "NAME_CHAPTER", chapter.getNAME_CHAPTER(),
                                                    "ID_MANGA_CHAPTER", chapter.getID_MANGA_CHAPTER(),
                                                    "MANGA_CHAPTER", chapter.getMANGA_CHAPTER(),
                                                    "PDF_CHAPTER", chapter.getPDF_CHAPTER());
                                            break; // Không cần tiếp tục vòng lặp sau khi tìm thấy chapter thích hợp
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
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
                            binding.OverlayIconFavorite.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42000000")));
                            binding.IconFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                            removeFromFavorite(mangaId);
                        } else {
                            // Manga is not in favorites, add it
                            binding.OverlayIconFavorite.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42F44336")));
                            binding.IconFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
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
                            binding.OverlayIconFavorite.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42F44336")));
                            binding.IconFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void loadPurchasedMangaIds(OnPurchasedMangaIdsLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("HistoryPayment")
                .child(mangaId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isPremium = true;
                if(snapshot.exists()){
                    binding.BuyBook.setText(R.string.Reading);
                    isPremium = false;
                }else if (!snapshot.exists() && mangaPremium){
                    binding.BuyBook.setText(R.string.Buy_book);
                }else isPremium = false;
                listener.onPurchasedMangaIdsLoaded(isPremium);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
    private void setTextItem() {
        binding.mangaDetailDescription.setText(intent.getStringExtra("DESCRIPTION_MANGA"));
        binding.mangaDetailTitle.setText(intent.getStringExtra("NAME_MANGA"));
        if (!isDestroyed()) {
            Glide.with(binding.mangaDetailImg)
                    .load(intent.getStringExtra("PICTURE_MANGA"))
                    .into(binding.mangaDetailImg);
        }

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