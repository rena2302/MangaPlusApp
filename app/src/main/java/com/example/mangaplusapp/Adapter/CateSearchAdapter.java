package com.example.mangaplusapp.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Fragment.MangaListFragment;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemCateSearchBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.util.filter.FilterCategory;

import java.util.List;

public class CateSearchAdapter extends RecyclerView.Adapter<CateSearchAdapter.CateSearchViewHolder> implements Filterable {
    private Context context;
    private List<Categories> categoryList, filterList;
    private FilterCategory filterCategory;
    private OnCategoryClickListener listener;
    public interface OnCategoryClickListener {
        void onCategoryClick(Categories category);
    }

    ItemCateSearchBinding binding;
    public void setData(List<Categories> categoryList){
        this.categoryList = categoryList;
        this.filterList =categoryList;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public CateSearchAdapter(List<Categories> categoryList, Context context){
        this.categoryList = categoryList;
        this.context = context;
    }

    public void setCategoryList(List<Categories> categoryList){
        this.categoryList = categoryList;
    }
    @NonNull
    @Override
    public CateSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCateSearchBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CateSearchViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CateSearchViewHolder holder, int position) {
        Categories category = categoryList.get(position);
        if(category == null){
            return;
        }
        holder.textView.setText(category.getNAME_CATEGORY());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(categoryList != null){
            return categoryList.size();
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

    public static class CateSearchViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public CateSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemSearchTxt);
        }
    }
}