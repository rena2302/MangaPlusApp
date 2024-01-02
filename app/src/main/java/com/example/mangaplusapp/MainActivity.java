package com.example.mangaplusapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;

import Adapter.TruyenTranhAdapter;
import object.TruyenTranh;

public class MainActivity extends AppCompatActivity {
    GridView gdvDSTruyen;
    TruyenTranhAdapter adapter;
    ArrayList<TruyenTranh> truyenTranhArrayList;
    EditText edtTimKiem;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FormLayout formLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // set content phải trước focus nha
        // Đang bị bug chồng layout
        focusFragment();

        // END FORM Validition
    }

    private void focusFragment(){ // ham chay ra main cua fragment
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int itemId = item.getItemId();
                if(itemId == R.id.navHome)
                {
                    loadFragment(new HomeFragment(),false);
                }
                else if (itemId == R.id.navHot){
                    loadFragment(new HotFragment(),false);
                }
                else if (itemId == R.id.navSearch){
                    loadFragment(new SearchFragment(),false);
                }
                else if (itemId == R.id.navCreator){
                    loadFragment(new CreatorFragment(),false);
                }
                else if (itemId == R.id.navLibrary){
                    loadFragment(new LibraryFragment(),false);
                }
                return true;
            }
        });
    }
    private void loadFragment (Fragment fragment,boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (isAppInitialized) {
                fragmentTransaction.add(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
            } else {
                fragmentTransaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
            }
        fragmentTransaction.commit();
    }

}