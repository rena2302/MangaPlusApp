package com.example.mangaplusapp.util.filter;

import android.widget.Filter;

import com.example.mangaplusapp.Adapter.FavoriteAdapter;
import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.object.Mangas;

import java.util.ArrayList;
import java.util.List;

public class FilterManga extends Filter {
    List<Mangas> truyenTranhList;
    FavoriteAdapter favoriteAdapter;

    public FilterManga(List<Mangas> truyenTranhList, FavoriteAdapter favoriteAdapter) {
        this.truyenTranhList = truyenTranhList;
        this.favoriteAdapter = favoriteAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        //value should not be null or empty
        if(constraint != null && constraint.length() > 0){
            //Change upper or lower
            constraint = constraint.toString().toUpperCase();
            List<Mangas> filterModels = new ArrayList<>();
            for(int i = 0; i < truyenTranhList.size(); i++){
                //validate
                if(truyenTranhList.get(i).getNAME_MANGA().toUpperCase().contains(constraint)){
                    //add to filterModels
                    filterModels.add(truyenTranhList.get(i));
                }
            }
            filterResults.count = filterModels.size();
            filterResults.values = filterModels;
        }else {
            filterResults.count = truyenTranhList.size();
            filterResults.values = truyenTranhList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        favoriteAdapter.setFilterManga((List<Mangas>)results.values);

        favoriteAdapter.notifyDataSetChanged();
    }
}
