package com.example.mangaplusapp.Activity.Base;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mangaplusapp.databinding.ActivityChapterPdfBinding;
import com.example.mangaplusapp.util.Constans;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ChapterPdfActivity extends AppCompatActivity{
    ActivityChapterPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String pdfURL = getIntent().getExtras().getString("PDF_CHAPTER");
        onClickEvent();
        loadChapterFromURL(pdfURL);

    }
    private void onClickEvent(){
        binding.pdfViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void loadChapterFromURL(String pdfURL) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfURL);
        reference.getBytes(Constans.MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        binding.pdfViewChapterName.setText(getIntent().getExtras().getString("NAME_CHAPTER"));
                        binding.pdfView.fromBytes(bytes)
                                .swipeHorizontal(false)// false = vertical , true = horizontal
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        int currentPage = (page + 1); // cuz start from 0
                                        binding.pdfViewPage.setText(currentPage + "/" + pageCount);

                                    }
                                })
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Toast.makeText(ChapterPdfActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        Toast.makeText(ChapterPdfActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .load();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}