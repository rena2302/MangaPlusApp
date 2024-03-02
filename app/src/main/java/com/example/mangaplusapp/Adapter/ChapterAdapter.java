package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.ChapterPdfActivity;
import com.example.mangaplusapp.Activity.MangaDetailActivity;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemDashboardBinding;
import com.example.mangaplusapp.object.Chapter;
import com.example.mangaplusapp.util.ActivityUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    ItemDashboardBinding binding;
    private Context context;
    private List<Chapter> chapterList;
    public void setData(Context context, List<Chapter> chapterList){
        this.context = context;
        this.chapterList = chapterList;
    }

    public ChapterAdapter() {
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemDashboardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ChapterViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        if(chapter == null){
            return;
        }
        holder.chapterTxt.setText(chapter.getNAME_CHAPTER());
        holder.chapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure delete this chapter")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_LONG).show();
                                deleteChapter(chapter);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startNewActivity(context, ChapterPdfActivity.class,
                        "ID_CHAPTER", chapter.getID_CHAPTER(),
                        "NAME_CHAPTER", chapter.getNAME_CHAPTER(),
                        "ID_MANGA_CHAPTER", chapter.getID_MANGA_CHAPTER(),
                        "MANGA_CHAPTER", chapter.getMANGA_CHAPTER(),
                        "PDF_CHAPTER", chapter.getPDF_CHAPTER());
            }
        });
    }
    private void deleteChapter(Chapter chapter) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
        String chapterId = chapter.getID_CHAPTER();
        reference.child(chapterId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Delete " + chapter.getNAME_CHAPTER() + " successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context," " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTxt;
        ImageButton chapterButton;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTxt = itemView.findViewById(R.id.itemDashText);
            chapterButton = itemView.findViewById(R.id.itemDashBtn);
        }
    }
}
