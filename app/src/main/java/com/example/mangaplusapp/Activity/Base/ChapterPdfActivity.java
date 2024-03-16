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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Adapter.ChapterAdapter;
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


public class ChapterPdfActivity extends BaseActivity{

    View view;
    ActivityChapterPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragmentBasic(new ChapterViewFragment(), false,
                "NAME_CHAPTER", getIntent().getStringExtra("NAME_CHAPTER"),
                "PDF_CHAPTER", getIntent().getStringExtra("PDF_CHAPTER"),
                "ID_MANGA_CHAPTER", getIntent().getStringExtra("ID_MANGA_CHAPTER"));
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
}