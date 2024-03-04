package com.example.mangaplusapp.util.filter;

import android.widget.Filter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Adapter.CateSearchAdapter;
import com.example.mangaplusapp.Adapter.DashBoardAdapter;
import com.example.mangaplusapp.object.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterCategory extends Filter {
    List<Category> categoryList;
    private RecyclerView.Adapter adapter;

    public FilterCategory(List<Category> categoryList, RecyclerView.Adapter adapter) {
        this.categoryList = categoryList;
        this.adapter = adapter;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        //value should not be null or empty
        if (constraint != null && constraint.length() > 0){
            //Change upper case or lower case
            constraint = constraint.toString().toUpperCase();
            List<Category> filterModels = new ArrayList<>();
            for ( int i = 0; i < categoryList.size(); i++){
                //validate
                if(categoryList.get(i).getNAME_CATEGORY().toUpperCase().contains(constraint)){
                    //add to filterModels
                    filterModels.add(categoryList.get(i));
                }
            }

            filterResults.count = filterModels.size();
            filterResults.values = filterModels;
        }
        else {
            filterResults.count = categoryList.size();
            filterResults.values = categoryList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
            if (adapter instanceof DashBoardAdapter) {
                ((DashBoardAdapter) adapter).setCategoryList((List<Category>) results.values);
                ((DashBoardAdapter) adapter).notifyDataSetChanged();
            }
            else if (adapter instanceof CateSearchAdapter){
                ((CateSearchAdapter) adapter).setCategoryList((List<Category>) results.values);
                ((CateSearchAdapter) adapter).notifyDataSetChanged();
            }
    }
}
