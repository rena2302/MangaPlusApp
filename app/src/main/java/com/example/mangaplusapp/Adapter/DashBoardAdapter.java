package com.example.mangaplusapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.Admin.EditorActivity;
import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Activity.Base.ChapterPdfActivity;
import com.example.mangaplusapp.Fragment.MangaListFragment;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemDashboardBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Chapters;
import com.example.mangaplusapp.util.ActivityUtils;
import com.example.mangaplusapp.util.filter.FilterCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder> implements Filterable {
    private Context context;
    private List<Categories> categoryList, filterList;
    private List<Chapters> chapterList;
    private ItemDashboardBinding binding;
    private FilterCategory filterCategory;
    public void setData(Context context, List<Categories> categoryList){
        this.context = context;
        this.categoryList = categoryList;
        this.filterList = categoryList;
    }
    public DashBoardAdapter(){
    }

    public DashBoardAdapter(Context context, List<Chapters> chapterList){
        this.context = context;
        this.chapterList = chapterList;
    }

    public void setCategoryList(List<Categories> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public DashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemDashboardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DashBoardViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardViewHolder holder, int position) {
        if(categoryList != null && !categoryList.isEmpty()){
            Categories category = categoryList.get(position);// Take item in category list at present position

            holder.dashText.setText(category.getNAME_CATEGORY());
            holder.dashButtonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("session_edit", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("session", "category");
                    editor.apply();
                    ActivityUtils.startNewActivityAndFinishCurrent(context, EditorActivity.class,
                            "ID_CATEGORY", category.getID_CATEGORY(),
                            "NAME_CATEGORY",category.getNAME_CATEGORY());
                }
            });
            holder.dashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.delete)
                            .setMessage(R.string.deleteConfirm)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, R.string.deleting, Toast.LENGTH_LONG).show();
                                    deleteCategory(category);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                    showCategoryDialog(category);
                }
            });
        }
        if(chapterList != null && !chapterList.isEmpty()){
            Chapters chapter = chapterList.get(position);
            holder.dashText.setText(chapter.getNAME_CHAPTER());
            holder.dashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle(R.string.delete)
                            .setMessage(R.string.deleteConfirm)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, R.string.deleting, Toast.LENGTH_LONG).show();
                                    deleteChapter(chapter);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
            holder.dashButtonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("session_edit", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("session", "chapter");
                    editor.apply();
                    ActivityUtils.startNewActivityAndFinishCurrent(context, EditorActivity.class,
                            "ID_CHAPTER", chapter.getID_CHAPTER(),
                            "NAME_CHAPTER", chapter.getNAME_CHAPTER(),
                            "ID_MANGA_CHAPTER", chapter.getID_MANGA_CHAPTER(),
                            "MANGA_CHAPTER", chapter.getMANGA_CHAPTER());
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
    }

    private void deleteChapter(Chapters chapter) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
        String chapterId = chapter.getID_CHAPTER();
        reference.child(chapterId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String deleteSuccessMessage = context.getString(R.string.deleteSuccess, chapter.getNAME_CHAPTER());
                        Toast.makeText(context,deleteSuccessMessage, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context," " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void showCategoryDialog(Categories category) {
         MangaListFragment dialogFragment = new MangaListFragment(category);
         Bundle args = new Bundle();
         args.putString("tag", "category_dialog");
         dialogFragment.setArguments(args);
         dialogFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "category_dialog");
    }

    private void deleteCategory(Categories category) {
        //get id from object
        String idCategory = category.getID_CATEGORY();
        //Attach to Firebase > Categories > ID_CATEGORY
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.child(idCategory)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String deleteSuccessMessage = context.getString(R.string.deleteSuccess, category.getNAME_CATEGORY());
                        Toast.makeText(context,deleteSuccessMessage, Toast.LENGTH_LONG).show();
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
        if (categoryList != null){
            return categoryList.size();
        }
        else if (chapterList != null){
            return chapterList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (filterCategory == null){
            filterCategory = new FilterCategory(filterList, this);
        }
        return filterCategory;
    }

    public static class DashBoardViewHolder extends RecyclerView.ViewHolder {
        TextView dashText;
        ImageButton dashButton;
        ImageButton dashButtonEdit;

        public DashBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            dashText = (TextView) itemView.findViewById(R.id.itemDashText);
            dashButton = (ImageButton) itemView.findViewById(R.id.itemDashBtn);
            dashButtonEdit = (ImageButton) itemView.findViewById(R.id.itemDashEditBtn);
        }
    }
}
