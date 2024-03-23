package com.example.mangaplusapp.Activity.Base;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Adapter.ChapterAdapter;
import com.example.mangaplusapp.Fragment.ChapterListFragment;
import com.example.mangaplusapp.Fragment.ChapterViewFragment;
import com.example.mangaplusapp.Fragment.HomeFragment;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityChapterPdfBinding;
import com.example.mangaplusapp.databinding.FragmentChapterViewBinding;
import com.example.mangaplusapp.databinding.FragmentEditorChapterBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Chapters;
import com.example.mangaplusapp.object.Mangas;
import com.example.mangaplusapp.util.Constans;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.color.utilities.CorePalette;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ChapterPdfActivity extends BaseActivity implements ChapterViewFragment.ActivityCallback{
    private boolean isPdfLoaded = false;
    String mangaId;
    View view;
    ActivityChapterPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mangaId = getIntent().getStringExtra("ID_MANGA_CHAPTER");
        onClickEvent();
        loadFragmentBasic(new ChapterViewFragment(), false,
                "NAME_CHAPTER", getIntent().getStringExtra("NAME_CHAPTER"),
                "PDF_CHAPTER", getIntent().getStringExtra("PDF_CHAPTER"),
                "ID_MANGA_CHAPTER", getIntent().getStringExtra("ID_MANGA_CHAPTER"));
    }
    private void showChapterDialog() {
        ChapterListFragment dialogFragment = new ChapterListFragment();
        Bundle args = new Bundle();
        args.putString("ID_MANGA_CHAPTER", mangaId);
        dialogFragment.setArguments(args);
        dialogFragment.setPdfLoaded(isPdfLoaded);
        // Sử dụng getChildFragmentManager() để quản lý Fragment trong Fragment
        dialogFragment.show(getSupportFragmentManager(), "chapter_dialog");
    }
    private void onClickEvent(){
        binding.pdfViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.pdfViewChapterSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChapterDialog();
            }
        });
    }
    private void loadFragmentBasic(Fragment fragment, boolean isAppInitialized, String... extras) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        for (int i = 0; i < extras.length; i += 2) {
            if (extras[i] != null && extras[i + 1] != null) {
                bundle.putString(extras[i], extras[i + 1]);
            }
        }
        // Gán Bundle vào Fragment
        fragment.setArguments(bundle);
        if (isAppInitialized) {
            fragmentTransaction.add(binding.pdfView.getId(), fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(binding.pdfView.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isPdfLoaded) {
            startNewActivityAndFinishCurrent(MainActivity.class);
            super.onBackPressed(); // Chỉ cho phép back khi PDF đã load xong
        } else {
            Toast.makeText(ChapterPdfActivity.this,R.string.mangaLoading, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void updatePdfLoadStatus(boolean isPdfLoaded) {
        this.isPdfLoaded = isPdfLoaded;
    }
}