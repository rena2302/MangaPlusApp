package com.example.mangaplusapp.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Activity.Admin.DashBoardAdminActivity;
import com.example.mangaplusapp.Adapter.CateSearchAdapter;
import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.Helper.DBHelper.CategoryDBHelper;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentSearchBinding;
import com.example.mangaplusapp.object.Category;
import com.example.mangaplusapp.object.TruyenTranh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    View view;
    TruyenTranhAdapter truyenTranhAdapter;
    CateSearchAdapter cateSearchAdapter;
    public interface OnMangaLoadedListener {
        void onMangaLoaded(List<TruyenTranh> truyenTranhList);
    }
    public interface OnCateLoadedListener {
        void onCateLoaded(List<Category> categoryList);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        truyenTranhAdapter = new TruyenTranhAdapter(new ArrayList<>(), view.getContext());
        cateSearchAdapter = new CateSearchAdapter(new ArrayList<>(), view.getContext());
        searchEvent();
        recyclerViewCate();
        recyclerViewManga();
        loadMangas(new OnMangaLoadedListener() {
            @Override
            public void onMangaLoaded(List<TruyenTranh> truyenTranhList) {
                truyenTranhAdapter.SetData(truyenTranhList);
                truyenTranhAdapter.notifyDataSetChanged();
            }
        });
        loadCategories(new OnCateLoadedListener() {
            @Override
            public void onCateLoaded(List<Category> categoryList) {
                cateSearchAdapter.setData(categoryList);
                cateSearchAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    private void loadMangas(OnMangaLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TruyenTranh> truyenTranhList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    TruyenTranh truyenTranh = ds.getValue(TruyenTranh.class);
                        truyenTranhList.add(truyenTranh);
                }
                // Gọi callback khi dữ liệu đã sẵn sàng
                listener.onMangaLoaded(truyenTranhList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "The loading mangas was interrupted",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadCategories(OnCateLoadedListener listener) {
        //Get all data from firebase > Categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        List<Category> categoryList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    Category category = ds.getValue(Category.class);
                    //add to List
                    categoryList.add(category);
                }
                listener.onCateLoaded(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recyclerViewManga(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.searchFmRcv);
        //set LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //set Adapter
        recyclerView.setAdapter(truyenTranhAdapter);
    }
    private void recyclerViewCate(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.searchFmRcvCate);
        //set LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //set Adapter
        recyclerView.setAdapter(cateSearchAdapter);
    }
    private void searchEvent() {
        EditText editText = (EditText) view.findViewById(R.id.inputManga);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //called as when user type each letters
                try {
                    truyenTranhAdapter.getFilter().filter(s);
                    cateSearchAdapter.getFilter().filter(s);
                }catch (Exception e){
                    Toast.makeText(getContext(), " " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}