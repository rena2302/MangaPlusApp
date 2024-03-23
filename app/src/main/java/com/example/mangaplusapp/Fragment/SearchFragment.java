package com.example.mangaplusapp.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Adapter.CateSearchAdapter;
import com.example.mangaplusapp.Adapter.FavoriteAdapter;
import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentSearchBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Mangas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    View view;
    FavoriteAdapter favoriteAdapter;
    CateSearchAdapter cateSearchAdapter;
    FragmentSearchBinding binding;

    public interface OnMangaLoadedListener {
        void onMangaLoaded(List<Mangas> truyenTranhList);
    }

    public interface OnCateLoadedListener {
        void onCateLoaded(List<Categories> categoryList);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        LinearLayout mainLayout= binding.OverlaySearch;
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(mainLayout,requireContext());
        favoriteAdapter = new FavoriteAdapter(new ArrayList<>(), getContext(), this);
        cateSearchAdapter = new CateSearchAdapter(new ArrayList<>(), getContext());
        searchEvent();
        recyclerViewCate();
        recyclerViewManga();
        loadMangas(new OnMangaLoadedListener() {
            @Override
            public void onMangaLoaded(List<Mangas> truyenTranhList) {
                favoriteAdapter.setData(truyenTranhList);
                favoriteAdapter.notifyDataSetChanged();
            }
        });
        loadCategories(new OnCateLoadedListener() {
            @Override
            public void onCateLoaded(List<Categories> categoryList) {
                cateSearchAdapter.setData(categoryList);
                cateSearchAdapter.notifyDataSetChanged();
            }
        });
        cateSearchAdapter.setOnCategoryClickListener(category -> {
            loadMangasByCategory(category, new OnMangaLoadedListener() {
                @Override
                public void onMangaLoaded(List<Mangas> truyenTranhList) {
                    favoriteAdapter.setData(truyenTranhList);
                    String searchBy = getContext().getString(R.string.search_by, category.getNAME_CATEGORY());
                    Toast.makeText(getContext(),searchBy, Toast.LENGTH_SHORT).show();
                    favoriteAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void loadMangasByCategory(Categories category, OnMangaLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.orderByChild("ID_CATEGORY_MANGA").equalTo(category.getID_CATEGORY()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Mangas> truyenTranhList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Mangas truyenTranh = ds.getValue(Mangas.class);
                    truyenTranhList.add(truyenTranh);
                }
                listener.onMangaLoaded(truyenTranhList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), R.string.loadingInterupted,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMangas(OnMangaLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Mangas> truyenTranhList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Mangas truyenTranh = ds.getValue(Mangas.class);
                        truyenTranhList.add(truyenTranh);
                }
                // Gọi callback khi dữ liệu đã sẵn sàng
                listener.onMangaLoaded(truyenTranhList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), R.string.loadingInterupted,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories(OnCateLoadedListener listener) {
        //Get all data from firebase > Categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        List<Categories> categoryList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    Categories category = ds.getValue(Categories.class);
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
        //set LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.searchFmRcv.setLayoutManager(gridLayoutManager);
        //set Adapter
        binding.searchFmRcv.setAdapter(favoriteAdapter);
    }

    private void recyclerViewCate(){
        //set LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.searchFmRcvCate.setLayoutManager(linearLayoutManager);
        //set Adapter
        binding.searchFmRcvCate.setAdapter(cateSearchAdapter);
    }

    private void searchEvent() {
        binding.inputManga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //called as when user type each letters
                try {
                    favoriteAdapter.getFilter().filter(s);
                    cateSearchAdapter.getFilter().filter(s);
                }catch (Exception e){
                    Toast.makeText(getContext(), " " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputManga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                } else {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });
    }
}