package com.example.mangaplusapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Admin.DashBoardAdminActivity;
import com.example.mangaplusapp.Activity.Admin.EditorActivity;
import com.example.mangaplusapp.Activity.Admin.MangaDetailAdminActivity;
import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.Activity.User.PaymentActivity;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.Mangas;
import com.example.mangaplusapp.util.ActivityUtils;
import com.example.mangaplusapp.util.filter.FilterManga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhAdapter.TruyenTranhViewHolder> implements Filterable {
    private Context context;
    private List<Mangas> truyenTranhList, filterList;
    private FilterManga filterManga;
    public void SetData(List<Mangas> truyenTranhList){
        this.truyenTranhList = truyenTranhList;
        this.filterList = truyenTranhList;
    }
    public TruyenTranhAdapter(){}
    public TruyenTranhAdapter(Context context){
        this.context = context;
    }
    public TruyenTranhAdapter(List<Mangas> truyenTranhList, Context context){
        this.truyenTranhList = truyenTranhList;
        this.context = context;
    }
    public void setFilterManga(List<Mangas> truyenTranhList) {this.truyenTranhList = truyenTranhList;}
    @NonNull
    @Override
    public TruyenTranhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new TruyenTranhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenTranhViewHolder holder, int position) {
        Mangas truyenTranh = truyenTranhList.get(position); // get item in truyentranhList at present position
        if (truyenTranh == null){
            return;
        }
        Glide.with(holder.itemView.getContext())
                .load(truyenTranh.getPICTURE_MANGA())
                .into(holder.imageTruyen);
        holder.txtTruyen.setText(truyenTranh.getNAME_MANGA());
        /*Code for DashBoardMangaListFragment*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof DashBoardAdminActivity){
                    ActivityUtils.startNewActivity(context, MangaDetailAdminActivity.class,
                            "ID_MANGA", truyenTranh.getID_MANGA(),
                            "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                            "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                            "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA());
                }else {
                    if(truyenTranh.isPREMIUM_MANGA()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Premium Manga")
                                .setMessage("If you wanna read this manga, you have to buy it")
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityUtils.startNewActivityAndFinishCurrent(context, PaymentActivity.class,
                                                "ID_MANGA", truyenTranh.getID_MANGA());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }else {
                        ActivityUtils.startNewActivity(context, MangaDetailActivity.class,
                                "ID_MANGA", truyenTranh.getID_MANGA(),
                                "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                                "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                                "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA());
                    }
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (context instanceof DashBoardAdminActivity){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Manga DashBoard")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("session_edit", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("session", "manga");
                                    editor.apply();
                                    ActivityUtils.startNewActivityAndFinishCurrent(context, EditorActivity.class,
                                            "ID_MANGA", truyenTranh.getID_MANGA(),
                                            "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                                            "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                                            "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA(),
                                            "CATEGORY_MANGA", truyenTranh.getCATEGORY_MANGA(),
                                            "ID_CATEGORY_MANGA", truyenTranh.getID_CATEGORY_MANGA(),
                                            "PREMIUM_MANGA", ""+truyenTranh.isPREMIUM_MANGA());
                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Deleting...", Toast.LENGTH_LONG).show();
                                    deleteMangas(truyenTranh);
                                }
                            })
                            .show();

                }
                return false;
            }
        });
    }
    private void deleteMangas(Mangas truyenTranh) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        String truyentranhId = truyenTranh.getID_MANGA();
        reference.child(truyentranhId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Delete " + truyenTranh.getNAME_MANGA() + " successfully", Toast.LENGTH_LONG).show();
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
        if(truyenTranhList != null){
            return truyenTranhList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(filterManga == null){
            filterManga = new FilterManga(filterList,this);
        }
        return filterManga;
    }

    public static class TruyenTranhViewHolder extends RecyclerView.ViewHolder{
        ImageView imageTruyen;
        TextView txtTruyen;
        public TruyenTranhViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTruyen = (ImageView) itemView.findViewById(R.id.imgAnhTruyen);
            txtTruyen = (TextView) itemView.findViewById(R.id.txvTenTruyen);
        }
    }
}