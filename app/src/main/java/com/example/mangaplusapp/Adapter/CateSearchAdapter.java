package com.example.mangaplusapp.Adapter;


import android.content.Context;
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
import com.example.mangaplusapp.object.Category;
import com.example.mangaplusapp.util.filter.FilterCategory;

import java.util.List;

public class CateSearchAdapter extends RecyclerView.Adapter<CateSearchAdapter.CateSearchViewHolder> implements Filterable {
    private Context context;
    private List<Category> categoryList, filterList;
    private FilterCategory filterCategory;
    ItemCateSearchBinding binding;
    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
        this.filterList =categoryList;
    }

    public CateSearchAdapter(List<Category> categoryList, Context context){
        this.categoryList = categoryList;
        this.context = context;
    }
    public void setCategoryList(List<Category> categoryList){
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
        Category category = categoryList.get(position);
        if(category == null){
            return;
        }
        holder.textView.setText(category.getNAME_CATEGORY());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(category);
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
    private void showCategoryDialog(Category category) {
        MangaListFragment dialogFragment = new MangaListFragment(category);
        dialogFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "category_dialog");
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