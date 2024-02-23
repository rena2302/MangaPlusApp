package com.example.mangaplusapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.ImageSliderAdapter;
import Adapter.TruyenTranhAdapter;
import Helper.DBHelper.MangaDBHelper;
import object.Category;
import object.TruyenTranh;
public class HomeFragment extends Fragment {
    View view;
    CategoryAdapter categoryAdapter;
    TruyenTranhAdapter truyenTranhAdapter;
    ImageSliderAdapter imageSliderAdapter;
    RecyclerView recyclerViewCategory;
    private static final int PERMISSION_REQUEST_READ_MEDIA_IMAGES = 1001;
    ViewPager2 viewPager2;
    Handler handler = new Handler();
    List<Category> categoryList = new ArrayList<Category>();
    List<TruyenTranh> truyenTranhList = new ArrayList<TruyenTranh>();
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar  = view.findViewById(R.id.main_header);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        AddListCnT();
        SetContentImageSlider();
        SetContentRecycleView();
        setRecyclerViewAnimation();
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
        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.imgslider_container_collapsing);
        AppBarLayout appBarLayout = view.findViewById(R.id.imgslider_container);
        Toolbar toolbar = view.findViewById(R.id.imgslider_toolbar);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isToolbarExpanded = true;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) scrollRange;
                if (percentage < 0.5) {
                    if (!isToolbarExpanded) {
                        isToolbarExpanded = true;
                    }
                } else {
                    if (isToolbarExpanded) {
                        isToolbarExpanded = false;
                    }
                }
            }
        });

    }
    private void AddListCnT(){
        MangaDBHelper db = new MangaDBHelper(getContext());
        List<TruyenTranh> truyenTranhList = db.getAllMangaItems();
        if (truyenTranhList != null) {
            for (TruyenTranh truyenTranh : truyenTranhList) {
                categoryList.add(new Category("Cate 1", truyenTranhList)); // test case
            }
        }
    }

    // Set content for recycleView
    private void SetContentRecycleView(){
        recyclerViewCategory = view.findViewById(R.id.rcv_category);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);// Set form for recycleView category

        categoryAdapter = new CategoryAdapter(view.getContext());
        categoryAdapter.SetData(categoryList);// set categoryList for adapter

        recyclerViewCategory.setAdapter(categoryAdapter);

        recyclerViewCategory.setNestedScrollingEnabled(false);

    }
    private void SetContentImageSlider(){
        viewPager2 = view.findViewById(R.id.vp2_image_slider);

        imageSliderAdapter = new ImageSliderAdapter(truyenTranhList, viewPager2);
        viewPager2.setAdapter(imageSliderAdapter);
        if(truyenTranhList==null){
            Log.d("Truyen tranh list ","==null " );
        }
        else{
            Log.d("Truyen tranh list ","== exists ");
        }
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
