package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.DashBoardAdminActivity;
import com.example.mangaplusapp.Activity.MangaDetailActivity;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.TruyenTranh;
import com.example.mangaplusapp.util.ActivityUtils;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhAdapter.TruyenTranhViewHolder>{
    private Context context;
    private List<TruyenTranh> truyenTranhList;
    private ViewPager2 viewPager2;
    public void SetData(List<TruyenTranh> truyenTranhList){
        this.truyenTranhList = truyenTranhList;
        notifyDataSetChanged();
    }

    public TruyenTranhAdapter(){}
    public TruyenTranhAdapter(List<TruyenTranh> truyenTranhList, Context context){
        this.truyenTranhList = truyenTranhList;
        this.context = context;
    }
    public TruyenTranhAdapter(List<TruyenTranh> truyenTranhList, ViewPager2 viewPager2){
        this.viewPager2 = viewPager2;
        this.truyenTranhList = truyenTranhList;
    }
    @NonNull
    @Override
    public TruyenTranhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new TruyenTranhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenTranhViewHolder holder, int position) {
        TruyenTranh truyenTranh = truyenTranhList.get(position); // get item in truyentranhList at present position
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
                ActivityUtils.startNewActivityAndFinishCurrent(context, MangaDetailActivity.class,
                        "ID_MANGA", truyenTranh.getID_MANGA(),
                        "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                        "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                        "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA());
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
        ImageView imageTruyen;
        TextView txtTruyen;
        public TruyenTranhViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTruyen = (ImageView) itemView.findViewById(R.id.imgAnhTruyen);
            txtTruyen = (TextView) itemView.findViewById(R.id.txvTenTruyen);
        }
    }
}