package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemChapterBinding;
import com.example.mangaplusapp.object.Chapters;
import com.example.mangaplusapp.util.ActivityUtils;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    ItemChapterBinding itemChapterBinding;
    private Context context;
    private List<Chapters> chapterList;
    private int selectedPosition = 0;
    public void setData(Context context, List<Chapters> chapterList){
        this.context = context;
        this.chapterList = chapterList;
    }

    public ChapterAdapter() {
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemChapterBinding = ItemChapterBinding.inflate(LayoutInflater.from(context), parent, false);
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
                holder.chapterButton.setChecked(true);
                ActivityUtils.startNewActivity(context, ChapterPdfActivity.class,
                        "ID_CHAPTER", chapter.getID_CHAPTER(),
                        "NAME_CHAPTER", chapter.getNAME_CHAPTER(),
                        "ID_MANGA_CHAPTER", chapter.getID_MANGA_CHAPTER(),
                        "MANGA_CHAPTER", chapter.getMANGA_CHAPTER(),
                        "PDF_CHAPTER", chapter.getPDF_CHAPTER());
            }
        });
    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTxt;
        CheckBox chapterButton;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTxt = itemView.findViewById(R.id.itemChapterText);
            chapterButton = itemView.findViewById(R.id.itemChapterCheck);
        }
    }
}
