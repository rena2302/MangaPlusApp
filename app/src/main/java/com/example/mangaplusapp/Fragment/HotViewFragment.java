package com.example.mangaplusapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mangaplusapp.Adapter.CategoryAdapter;
import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentHotViewBinding;
import com.example.mangaplusapp.object.Mangas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HotViewFragment extends Fragment {
    FragmentHotViewBinding binding;
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Mangas> truyenTranhList);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHotViewBinding.inflate(inflater);
        loadTopView(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Mangas> truyenTranhList) {
                SetContentRecycleView(truyenTranhList);
            }
        });
        return binding.getRoot();
    }
    private void SetContentRecycleView(List<Mangas> mangasList){
        TruyenTranhAdapter adapter = new TruyenTranhAdapter(getContext(),this);
        adapter.SetData(mangasList);
        binding.hotFmViewRcv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.hotFmViewRcv.setAdapter(adapter);

    }
    private void loadTopView(OnDataLoadedListener listener){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.orderByChild("VIEW_MANGA")
                .limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    List<Mangas> mangasList = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Mangas manga = dataSnapshot.getValue(Mangas.class);
                            mangasList.add(manga);
                        }
                        Collections.reverse(mangasList);
                        listener.onDataLoaded(mangasList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}