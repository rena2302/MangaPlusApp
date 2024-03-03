package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemChapterBinding;
import com.example.mangaplusapp.databinding.ItemDashboardBinding;
import com.example.mangaplusapp.object.Chapter;
import com.example.mangaplusapp.util.ActivityUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    ItemChapterBinding itemChapterBinding;
    private Context context;
    private List<Chapter> chapterList;
    private int selectedPosition = 0;
    public void setData(Context context, List<Chapter> chapterList){
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
        Chapter chapter = chapterList.get(position);
        if(chapter == null){
            return;
        }
        holder.chapterTxt.setText(chapter.getNAME_CHAPTER());

        boolean isChecked = false; // Mặc định không được chọn
        if (selectedPosition == position) {
            isChecked = true;
        }

        // Thiết lập trạng thái của chapterButton
        holder.chapterButton.setChecked(isChecked);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.chapterButton.setChecked(true);
                selectedPosition = holder.getAdapterPosition();
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
