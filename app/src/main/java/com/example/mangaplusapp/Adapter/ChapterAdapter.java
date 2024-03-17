package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
import com.example.mangaplusapp.Fragment.ChapterViewFragment;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityChapterPdfBinding;
import com.example.mangaplusapp.databinding.ItemChapterBinding;
import com.example.mangaplusapp.object.Chapters;
import com.example.mangaplusapp.util.ActivityUtils;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    ItemChapterBinding itemChapterBinding;
    ActivityChapterPdfBinding activityChapterPdfBinding;
    private boolean isPdfLoaded = false;

    private Context context;
    private List<Chapters> chapterList;
    private FragmentManager fragmentManager;
    public void setData(List<Chapters> chapterList){
        this.chapterList = chapterList;
    }

    public ChapterAdapter(List<Chapters> chapterList, Context context, boolean isPdfLoaded, FragmentManager fragmentManager) {
        this.chapterList = chapterList;
        this.context = context;
        this.fragmentManager = fragmentManager; // Lưu tham chiếu tới FragmentManager
        this.isPdfLoaded = isPdfLoaded;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemChapterBinding = ItemChapterBinding.inflate(LayoutInflater.from(context), parent, false);
        activityChapterPdfBinding = ActivityChapterPdfBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ChapterViewHolder(itemChapterBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapters chapter = chapterList.get(position);
        if(chapter == null){
            return;
        }
        holder.chapterTxt.setText(chapter.getNAME_CHAPTER());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPdfLoaded) {
                    loadFragmentBasic(fragmentManager, new ChapterViewFragment(), false,
                            "ID_CHAPTER", chapter.getID_CHAPTER(),
                            "NAME_CHAPTER", chapter.getNAME_CHAPTER(),
                            "ID_MANGA_CHAPTER", chapter.getID_MANGA_CHAPTER(),
                            "MANGA_CHAPTER", chapter.getMANGA_CHAPTER(),
                            "PDF_CHAPTER", chapter.getPDF_CHAPTER());
                    Toast.makeText(context, "Are in " + chapter.getNAME_CHAPTER(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "PDF is not loaded yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }
    private void loadFragmentBasic(FragmentManager fragmentManager, Fragment fragment, boolean isAppInitialized, String... extras) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        for (int i = 0; i < extras.length; i += 2) {
            if (extras[i] != null && extras[i + 1] != null) {
                bundle.putString(extras[i], extras[i + 1]);
            }
        }
        fragment.setArguments(bundle);
        if (isAppInitialized) {
            fragmentTransaction.add(activityChapterPdfBinding.pdfView.getId(), fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(activityChapterPdfBinding.pdfView.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTxt;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTxt = itemView.findViewById(R.id.itemChapterText);
        }
    }
}
