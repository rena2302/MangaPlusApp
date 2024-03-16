package com.example.mangaplusapp.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.mangaplusapp.Adapter.CategoryAdapter;
import com.example.mangaplusapp.Adapter.ImageSliderAdapter;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Mangas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    View view;
    CategoryAdapter categoryAdapter;
    ImageSliderAdapter imageSliderAdapter;
    RecyclerView recyclerViewCategory;
    private static final int PERMISSION_REQUEST_READ_MEDIA_IMAGES = 1001;
    ViewPager2 viewPager2;
    Handler handler = new Handler();
    List<Categories> categoryList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Mangas> truyenTranhList);
    }
    public interface OnPurchasedMangaIdsLoadedListener {
        void onPurchasedMangaIdsLoaded(List<String> purchasedMangaIds);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar  = view.findViewById(R.id.main_header);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        AddListCnT();
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu quyền từ người dùng
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_READ_MEDIA_IMAGES);
            }
        } else {
            // Quyền đã được cấp, tiến hành thực hiện các hoạt động liên quan đến hình ảnh
            // Ví dụ: hiển thị danh sách hình ảnh từ thư viện phương tiện
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void setRecyclerViewAnimation(){
        AppBarLayout appBarLayout = view.findViewById(R.id.imgslider_container);
        NestedScrollView nestedScrollView = view.findViewById(R.id.home_fm_content);
        RecyclerView recyclerView = view.findViewById(R.id.rcv_category);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        boolean appBarExpanded = appBarLayout.getHeight() + verticalOffset == 0;
                        nestedScrollView.setNestedScrollingEnabled(appBarExpanded);
                    }
                });
            }
        });
    }
    private void AddListCnT(){
        loadCategories();
    }
    private void loadMangas(Categories category, OnDataLoadedListener listener) {
        //Get all data from firebase > Categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.orderByChild("ID_CATEGORY_MANGA").equalTo(category.getID_CATEGORY())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Mangas> mangaForCategory = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    Mangas truyenTranh = ds.getValue(Mangas.class);
                    mangaForCategory.add(truyenTranh);
                }
                listener.onDataLoaded(mangaForCategory);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadCategories() {

        //Get all data from firebase > Categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                int categoryCount = (int) snapshot.getChildrenCount();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    Categories category = ds.getValue(Categories.class);
                    //add to List
                    loadMangas(category, new OnDataLoadedListener() {
                        @Override
                        public void onDataLoaded(List<Mangas> truyenTranhList) {
                            category.setTruyenTranhList(truyenTranhList);
                            categoryList.add(category);
                            if (categoryList.size() == categoryCount) {
                                SetContentImageSlider();
                                SetContentRecycleView();
                                setRecyclerViewAnimation();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Set content for recycleView
    private void SetContentRecycleView(){
        recyclerViewCategory = view.findViewById(R.id.rcv_category);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);// Set form for recycleView category

        categoryAdapter = new CategoryAdapter(view.getContext());
        categoryAdapter.SetData(categoryList);// set categoryList for adapter

        recyclerViewCategory.setAdapter(categoryAdapter);

        recyclerViewCategory.setNestedScrollingEnabled(false);

    }
    private void SetContentImageSlider(){
        List<Mangas> truyenTranhList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Mangas truyenTranh = ds.getValue(Mangas.class);
                    truyenTranhList.add(truyenTranh);
                }
                viewPager2 = view.findViewById(R.id.vp2_image_slider);

                imageSliderAdapter = new ImageSliderAdapter(getContext(),truyenTranhList, viewPager2);
                viewPager2.setAdapter(imageSliderAdapter);

                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.85f + r * 0.15f);
                    }
                });
                viewPager2.setPageTransformer(compositePageTransformer);

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        handler.removeCallbacks(sliderRunnable);
                        handler.postDelayed(sliderRunnable, 5000);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, tiến hành thực hiện các hoạt động liên quan đến hình ảnh
                // Ví dụ: hiển thị danh sách hình ảnh từ thư viện phương tiện
            } else {
                // Người dùng từ chối cấp quyền, thông báo cho họ về việc không thể thực hiện các hoạt động liên quan đến hình ảnh
            }
        }
    }
}
