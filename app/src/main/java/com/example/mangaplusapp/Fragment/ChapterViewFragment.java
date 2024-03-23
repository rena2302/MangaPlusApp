package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Adapter.ChapterAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityChapterPdfBinding;
import com.example.mangaplusapp.databinding.ActivityPaymentBinding;
import com.example.mangaplusapp.databinding.FragmentChapterViewBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Chapters;
import com.example.mangaplusapp.util.ActivityUtils;
import com.example.mangaplusapp.util.Constans;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChapterViewFragment extends Fragment {
    FragmentChapterViewBinding chapterViewBinding;
    Bundle args;
    private ActivityCallback activityCallback;
    public interface ActivityCallback {
        void updatePdfLoadStatus(boolean isPdfLoaded);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activityCallback = (ActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ActivityCallback");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chapterViewBinding = FragmentChapterViewBinding.inflate(getLayoutInflater());
        args = getArguments();
        String pdfURL = args.getString("PDF_CHAPTER");
        activityCallback.updatePdfLoadStatus(false);
        loadChapterFromURL(pdfURL);
        return chapterViewBinding.getRoot();

    }

    private void loadChapterFromURL(String pdfURL) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfURL);
        reference.getBytes(Constans.MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        chapterViewBinding.pdfView.fromBytes(bytes)
                                .swipeHorizontal(false)// false = vertical , true = horizontal
                                .onLoad(new OnLoadCompleteListener() {
                                    @Override
                                    public void loadComplete(int nbPages) {
                                        activityCallback.updatePdfLoadStatus(true);
                                        chapterViewBinding.pdfViewChapterName.setText(args.getString("NAME_CHAPTER"));
                                    }
                                })
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        int currentPage = (page + 1); // cuz start from 0
                                        chapterViewBinding.pdfViewPage.setText(currentPage + "/" + pageCount);
                                        chapterViewBinding.pdfViewProgressBar.setMax(pageCount);
                                        chapterViewBinding.pdfViewProgressBar.setProgress(currentPage);

                                    }
                                })
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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