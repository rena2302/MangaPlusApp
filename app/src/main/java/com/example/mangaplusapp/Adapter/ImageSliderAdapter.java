package com.example.mangaplusapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.Activity.User.PaymentActivity;
import com.example.mangaplusapp.R;

import java.util.List;

import com.example.mangaplusapp.object.Mangas;
import com.example.mangaplusapp.util.ActivityUtils;

public class ImageSliderAdapter extends  RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    private Context context;
    private final List<Mangas> imageSlidersList;
    private ViewPager2 viewPager2;
    public ImageSliderAdapter(Context context, List<Mangas> list, ViewPager2 viewPager2){
        this.imageSlidersList = list;
        this.viewPager2 = viewPager2;
        this.context = context;
    }
    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider,parent,false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        Mangas imageSlider = imageSlidersList.get(position);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageSlider.isPREMIUM_MANGA()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Premium Manga")
                            .setMessage("If you wanna read this manga, you have to buy it")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityUtils.startNewActivityAndFinishCurrent(context, PaymentActivity.class);
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
                            "ID_MANGA", imageSlider.getID_MANGA(),
                            "NAME_MANGA", imageSlider.getNAME_MANGA(),
                            "PICTURE_MANGA", imageSlider.getPICTURE_MANGA(),
                            "DESCRIPTION_MANGA", imageSlider.getDESCRIPTION_MANGA());
                }
            }
        });
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
