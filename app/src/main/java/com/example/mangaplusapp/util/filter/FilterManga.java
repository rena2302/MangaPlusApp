package com.example.mangaplusapp.util.filter;

import android.widget.Filter;

import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.object.Mangas;

import java.util.ArrayList;
import java.util.List;

public class FilterManga extends Filter {
    List<Mangas> truyenTranhList;
    TruyenTranhAdapter truyenTranhAdapter;

    public FilterManga(List<Mangas> truyenTranhList, TruyenTranhAdapter truyenTranhAdapter) {
        this.truyenTranhList = truyenTranhList;
        this.truyenTranhAdapter = truyenTranhAdapter;
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
        truyenTranhAdapter.setFilterManga((List<Mangas>)results.values);

        truyenTranhAdapter.notifyDataSetChanged();
    }
}
