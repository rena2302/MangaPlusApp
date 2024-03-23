package com.example.mangaplusapp.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.Adapter.FavoriteAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentFavoriteBinding;
import com.example.mangaplusapp.object.Mangas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteFragment extends Fragment {
    FragmentFavoriteBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FavoriteAdapter adapter;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Mangas> truyenTranhList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        loadMangas(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Mangas> truyenTranhList) {
                setRecyclerView(truyenTranhList);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setRecyclerView(List<Mangas> mangasList){
        adapter = new FavoriteAdapter(getContext());
        adapter.setData(mangasList);
        binding.favoFmRcv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.favoFmRcv.setAdapter(adapter);

    }

    private void loadMangas(OnDataLoadedListener listener){
        String uid = currentUser.getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(uid).child("Favorites");
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> mangaIds = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String mangaId = dataSnapshot.child("ID_MANGA").getValue(String.class);
                    mangaIds.add(mangaId);
                }

                // Tạo một danh sách tạm thời để lưu trữ dữ liệu manga
                List<Mangas> mangasList = new ArrayList<>();
                DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("Mangas");
                for (String mangaId : mangaIds) {
                    mangaRef.orderByChild("ID_MANGA").equalTo(mangaId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot mangaSnapshot : snapshot.getChildren()) {
                                        Mangas mangas = mangaSnapshot.getValue(Mangas.class);
                                        mangasList.add(mangas);
                                    }

                                    // Kiểm tra xem đã tải tất cả dữ liệu chưa
                                    if (mangasList.size() == mangaIds.size()) {
                                        listener.onDataLoaded(mangasList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), R.string.loadingInterupted, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), R.string.loadingInterupted, Toast.LENGTH_SHORT).show();
            }
        });
    }
}