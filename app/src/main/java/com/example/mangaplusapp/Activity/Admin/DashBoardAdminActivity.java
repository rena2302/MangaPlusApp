package com.example.mangaplusapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Adapter.DashBoardAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityDashBoardAdminBinding;
import com.example.mangaplusapp.object.Categories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashBoardAdminActivity extends BaseActivity {
    private ActivityDashBoardAdminBinding binding;
    ImageButton shutDown;
    DashBoardAdapter dashBoardAdapter = new DashBoardAdapter(this, new ArrayList<>());
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Categories> categoryList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onClickEvent();

        //setup LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext(), RecyclerView.VERTICAL,false);
        binding.adminRcvContainer.setLayoutManager(linearLayoutManager);

        //set adapter
        binding.adminRcvContainer.setAdapter(dashBoardAdapter);
        backToMain();
        loadCategories(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Categories> categoryList) {
                dashBoardAdapter.setData(DashBoardAdminActivity.this, categoryList);
                searchEvent();
                dashBoardAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchEvent() {
        binding.adminDashSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //called as when user type each letters
                try {
                    dashBoardAdapter.getFilter().filter(s);
                }catch (Exception e){
                    Toast.makeText(binding.getRoot().getContext(), " " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadCategories(OnDataLoadedListener listener) {
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
                listener.onDataLoaded(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void onClickEvent(){
        binding.adminAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardAdminActivity.this, CategoryAddActivity.class));
                finish();
            }
        });
        binding.adminAddManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardAdminActivity.this, MangaAddActivity.class));
                finish();
            }
        });
        binding.addChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardAdminActivity.this, ChapterAddActivity.class));
                finish();
            }
        });
    }

    private void backToMain()
    {
        shutDown=findViewById(R.id.shutDownAdmin);
        shutDown.setOnClickListener(v->{
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}