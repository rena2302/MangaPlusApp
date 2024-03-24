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
import com.example.mangaplusapp.Fragment.HotBoughtFragment;
import com.example.mangaplusapp.Fragment.HotFragment;
import com.example.mangaplusapp.Fragment.HotViewFragment;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.Mangas;
import com.example.mangaplusapp.util.ActivityUtils;
import com.example.mangaplusapp.util.filter.FilterManga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhAdapter.TruyenTranhViewHolder>{
    private Context context;
    private View view;
    private List<Mangas> truyenTranhList;
    private HotViewFragment hotViewFragment;
    private HotBoughtFragment hotBoughtFragment;

    public void SetData(List<Mangas> truyenTranhList){
        this.truyenTranhList = truyenTranhList;
    }

    public TruyenTranhAdapter(Context context, HotViewFragment hotFragment){
        this.hotViewFragment = hotFragment;
        this.context = context;
    }

    public TruyenTranhAdapter(Context context, HotBoughtFragment hotBoughtFragment){
        this.hotBoughtFragment = hotBoughtFragment;
        this.context = context;
    }

    public TruyenTranhAdapter(Context context){
        this.context = context;
    }

    public TruyenTranhAdapter(List<Mangas> truyenTranhList, Context context){
        this.truyenTranhList = truyenTranhList;
        this.context = context;
    }

    @NonNull
    @Override
    public TruyenTranhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (hotViewFragment != null || hotBoughtFragment != null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        }
        return new TruyenTranhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenTranhViewHolder holder, int position) {
        Mangas truyenTranh = truyenTranhList.get(position); // get item in truyentranhList at present position
        if (truyenTranh == null){
            return;
        }
        if (hotViewFragment != null || hotBoughtFragment != null){
            Glide.with(holder.itemView.getContext())
                    .load(truyenTranh.getPICTURE_MANGA())
                    .into(holder.imageHot);
            holder.txtHot.setText(truyenTranh.getNAME_MANGA());
            String viewCount = context.getString(R.string.viewHot)+ ": " + truyenTranh.getVIEW_MANGA();
            String boughtCount = context.getString(R.string.boughtHot)+ ": " + truyenTranh.getBOUGHT_MANGA();
            if(hotViewFragment != null)holder.txtView.setText(viewCount);
            else holder.txtView.setText(boughtCount);

            holder.txtRank.setText(String.valueOf(position + 1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startNewActivity(context, MangaDetailActivity.class,
                            "ID_MANGA", truyenTranh.getID_MANGA(),
                            "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                            "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                            "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA(),
                            "PREMIUM_MANGA",String.valueOf(truyenTranh.isPREMIUM_MANGA()),
                            "VIEW_MANGA", String.valueOf(truyenTranh.getVIEW_MANGA()),
                            "BOUGHT_MANGA",String.valueOf(truyenTranh.getBOUGHT_MANGA()),
                            "PRICE_MANGA", truyenTranh.getPRICE_MANGA());
                }
            });
        }else {
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
                        ActivityUtils.startNewActivity(context, MangaDetailActivity.class,
                                "ID_MANGA", truyenTranh.getID_MANGA(),
                                "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                                "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                                "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA(),
                                "PREMIUM_MANGA",String.valueOf(truyenTranh.isPREMIUM_MANGA()),
                                "VIEW_MANGA", String.valueOf(truyenTranh.getVIEW_MANGA()),
                                "BOUGHT_MANGA",String.valueOf(truyenTranh.getBOUGHT_MANGA()),
                                "PRICE_MANGA", truyenTranh.getPRICE_MANGA());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (context instanceof DashBoardAdminActivity){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Manga DashBoard")
                                .setPositiveButton(R.string.EDIT, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("session_edit", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("session", "manga");
                                        editor.apply();
                                        ActivityUtils.startNewActivity(context, EditorActivity.class,
                                                "ID_MANGA", truyenTranh.getID_MANGA(),
                                                "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                                                "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                                                "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA(),
                                                "CATEGORY_MANGA", truyenTranh.getCATEGORY_MANGA(),
                                                "ID_CATEGORY_MANGA", truyenTranh.getID_CATEGORY_MANGA(),
                                                "PREMIUM_MANGA", ""+truyenTranh.isPREMIUM_MANGA(),
                                                "PRICE_MANGA",truyenTranh.getPRICE_MANGA());
                                    }
                                })
                                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, R.string.deleting, Toast.LENGTH_LONG).show();
                                        deleteMangas(truyenTranh);
                                    }
                                })
                                .show();
                    }
                    return false;
                }
            });
        }
    }

    private void deleteMangas(Mangas truyenTranh) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        String truyentranhId = truyenTranh.getID_MANGA();
        reference.child(truyentranhId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String delete = context.getString(R.string.deleteSuccess, truyenTranh.getNAME_MANGA());
                        Toast.makeText(context, delete , Toast.LENGTH_LONG).show();
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

    public static class TruyenTranhViewHolder extends RecyclerView.ViewHolder{
        ImageView imageTruyen, imageHot;
        TextView txtTruyen, txtHot, txtView, txtRank;

        public TruyenTranhViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHot = (ImageView) itemView.findViewById(R.id.itemHotImg);
            txtHot = (TextView) itemView.findViewById(R.id.itemHotName);
            txtView = (TextView) itemView.findViewById(R.id.itemHotView);
            txtRank = (TextView) itemView.findViewById(R.id.itemHotRank);

            imageTruyen = (ImageView) itemView.findViewById(R.id.imgAnhTruyen);
            txtTruyen = (TextView) itemView.findViewById(R.id.txvTenTruyen);
        }
    }
}