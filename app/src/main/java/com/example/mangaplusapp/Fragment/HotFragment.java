package com.example.mangaplusapp.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mangaplusapp.Adapter.HotAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentHotBinding;
import com.example.mangaplusapp.object.Mangas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HotFragment extends Fragment {
    FragmentHotBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        binding = FragmentHotBinding.inflate(getLayoutInflater());
        HotAdapter hotAdapter = new HotAdapter(requireActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        hotAdapter.setData(getContext());
        binding.hotFmViewPager.setAdapter(hotAdapter);
        binding.hotFmTab.setupWithViewPager(binding.hotFmViewPager);
    }
}