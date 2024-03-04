package com.example.mangaplusapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Admin.DashBoardAdminActivity;
import com.example.mangaplusapp.Activity.Admin.MangaDetailAdminActivity;
import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.Category;
import com.example.mangaplusapp.object.TruyenTranh;
import com.example.mangaplusapp.util.ActivityUtils;
import com.example.mangaplusapp.util.filter.FilterManga;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhAdapter.TruyenTranhViewHolder> implements Filterable {
    private Context context;
    private List<TruyenTranh> truyenTranhList, filterList;
    private FilterManga filterManga;
    public void SetData(List<TruyenTranh> truyenTranhList){
        this.truyenTranhList = truyenTranhList;
        this.filterList = truyenTranhList;
    }
    public TruyenTranhAdapter(){}
    public TruyenTranhAdapter(Context context){
        this.context = context;
    }
    public TruyenTranhAdapter(List<TruyenTranh> truyenTranhList, Context context){
        this.truyenTranhList = truyenTranhList;
        this.context = context;
    }
    public void setFilterManga(List<TruyenTranh> truyenTranhList) {this.truyenTranhList = truyenTranhList;}
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
                if (context instanceof DashBoardAdminActivity){
                    ActivityUtils.startNewActivityAndFinishCurrent(context, MangaDetailAdminActivity.class,
                            "ID_MANGA", truyenTranh.getID_MANGA(),
                            "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                            "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                            "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA());
                }else {
                    ActivityUtils.startNewActivityAndFinishCurrent(context, MangaDetailActivity.class,
                            "ID_MANGA", truyenTranh.getID_MANGA(),
                            "NAME_MANGA", truyenTranh.getNAME_MANGA(),
                            "PICTURE_MANGA", truyenTranh.getPICTURE_MANGA(),
                            "DESCRIPTION_MANGA", truyenTranh.getDESCRIPTION_MANGA());
                }
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