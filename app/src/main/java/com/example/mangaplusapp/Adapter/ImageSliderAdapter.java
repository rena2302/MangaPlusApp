package com.example.mangaplusapp.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.TruyenTranh;

public class ImageSliderAdapter extends  RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    private final List<TruyenTranh> imageSlidersList;
    private ViewPager2 viewPager2;
    public ImageSliderAdapter(List<TruyenTranh> list, ViewPager2 viewPager2){
        this.imageSlidersList = list;
        this.viewPager2 = viewPager2;
    }
    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider,parent,false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        TruyenTranh imageSlider = imageSlidersList.get(position);
        if(imageSlider == null) {
            return;
        }
        if (position == getItemCount() - 2){
            viewPager2.post(runnable);
        }
        holder.textSlider.setText(imageSlider.getNAME_MANGA());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(imageSlider.getPICTURE_MANGA()))
                .into(holder.imageSlider);
    }

    @Override
    public int getItemCount() {
        return imageSlidersList.size();
    }

    public static class ImageSliderViewHolder extends RecyclerView.ViewHolder{
        ImageView imageSlider;
        TextView textSlider;
        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlider = itemView.findViewById(R.id.imgslider);
            textSlider = itemView.findViewById(R.id.textslider);
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageSlidersList.addAll(imageSlidersList);
            notifyDataSetChanged();
        }
    };
}
