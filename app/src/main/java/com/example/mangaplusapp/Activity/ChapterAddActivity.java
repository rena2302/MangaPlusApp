package com.example.mangaplusapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityChapterAddBinding;
import com.example.mangaplusapp.object.Category;
import com.example.mangaplusapp.object.TruyenTranh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChapterAddActivity extends AppCompatActivity {
    ActivityChapterAddBinding binding;
    List<TruyenTranh> truyenTranhList;
    String selectedMangaId;
    private static final int PICK_PDF_FILE = 1;
    private Uri pdfUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadMangas();
        onClickEvent();
    }

    private void loadMangas() {
        //get mangas from firebase
        truyenTranhList = new ArrayList<>();
        //load fire db > mangas --> mangaList
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                truyenTranhList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    //get Data
                    TruyenTranh truyenTranh = ds.getValue(TruyenTranh.class);
                    //add to list
                    truyenTranhList.add(truyenTranh);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChapterAddActivity.this, "The loading mangas was interrupted",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickEvent() {
        binding.addChapterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.addChapterPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPdfFile();
            }
        });
        binding.addChapterManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaPickDialog();
            }
        });
    }
    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_FILE);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(this, "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String chapter = "";
    private void validateData() {
        /*before adding validate data*/

        //get Data
        chapter = binding.addChapterName.getText().toString().trim();
        if(TextUtils.isEmpty(chapter)){
            Toast.makeText(this,"Please enter chapter name...!", Toast.LENGTH_SHORT).show();
        }
        else {
            addChapterFireBase();
        }
    }
    private void mangaPickDialog() {
        //get string Array from categoryList
        String[] mangasString = new String[truyenTranhList.size()];
        for (int i = 0; i < truyenTranhList.size(); i++){
            mangasString[i] = truyenTranhList.get(i).getNAME_MANGA();
        }

        //alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Pick manga")
                .setItems(mangasString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item click
                        //get clicked item from list
                        String manga = mangasString[which];
                        //set category to textView
                        binding.addChapterManga.setText(manga);

                        selectedMangaId = truyenTranhList.get(which).getID_MANGA();
                    }
                })
                .show();
    }
    private void addChapterFireBase() {
        long timestamp = System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("ID_CHAPTER", ""+timestamp);
        hashMap.put("NAME_CHAPTER", ""+chapter);
        hashMap.put("MANGA_CHAPTER", ""+binding.addChapterManga.getText());
        hashMap.put("ID_MANGA_CHAPTER", ""+selectedMangaId);
        //hashMap.put("UID_MANGA", firebaseAuth.getUid());
        // Upload PDF file to Firebase Storage
        if (pdfUri != null) {
            // Get reference to storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("chapter_pdfs").child(""+timestamp+".pdf");

            // Upload file
            storageReference.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get URL of uploaded file
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String pdfUrl = uri.toString();
                                    // Add URL to database
                                    hashMap.put("PDF_CHAPTER", pdfUrl);
                                    // Add chapter to Firebase Database
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
                                    reference.child(""+timestamp)
                                            .setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ChapterAddActivity.this,"Chapter add successful", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ChapterAddActivity.this,"Chapter add fail", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChapterAddActivity.this,"PDF upload failed", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(ChapterAddActivity.this,"Please select a PDF file", Toast.LENGTH_LONG).show();
        }
    }
}