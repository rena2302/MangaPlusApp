package com.example.mangaplusapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.Database.User;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Mangas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MangaListFragment extends DialogFragment {
    private Categories category;
    private View view;
    public MangaListFragment() {}
    public MangaListFragment(Categories category){
        this.category = category;
    }
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Mangas> truyenTranhList);
    }
    private String tag;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Lấy Bundle chứa dữ liệu từ nơi gọi
        Bundle args = getArguments();
        if (args != null) {
            tag = args.getString("tag");
            // Sử dụng giá trị tag ở đây
        }
        // Khởi tạo adapter trước khi hiển thị dialog
        TruyenTranhAdapter adapter = new TruyenTranhAdapter(new ArrayList<>(), getContext());
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dashboard_mangalist, null);
        RecyclerView recyclerView = view.findViewById(R.id.dashFragmentRcv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        if(category != null){
            builder
                    .setView(view)
                    .setTitle(category.getNAME_CATEGORY())
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }else {
            builder
                    .setView(view)
                    .setTitle("Favorites")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

        }


        // Load dữ liệu và cập nhật adapter khi dữ liệu đã sẵn sàng
        loadMangas(tag, new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Mangas> truyenTranhList) {
                adapter.SetData(truyenTranhList);
                adapter.notifyDataSetChanged();
            }
        });

        return builder.create();
    }
    private void loadMangas(String tag, OnDataLoadedListener listener) {
        if (tag.equals("Favorite")) {
            // Load danh sách manga được người dùng đánh dấu là yêu thích
            if (currentUser != null) {
                String uid = currentUser.getUid();
                DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid).child("Favorites");

                favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> mangaIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String mangaId = snapshot.child("ID_MANGA").getValue(String.class);
                            mangaIds.add(mangaId);
                        }

                        // Tạo một danh sách tạm thời để lưu trữ dữ liệu manga
                        List<Mangas> mangasList = new ArrayList<>();
                        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("Mangas");
                        for (String mangaId : mangaIds) {
                            mangaRef.orderByChild("ID_MANGA").equalTo(mangaId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot mangaSnapshot : dataSnapshot.getChildren()) {
                                                Mangas mangas = mangaSnapshot.getValue(Mangas.class);
                                                mangasList.add(mangas);
                                            }

                                            // Kiểm tra xem đã tải tất cả dữ liệu chưa
                                            if (mangasList.size() == mangaIds.size()) {
                                                listener.onDataLoaded(mangasList);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý lỗi nếu cần
                                            Toast.makeText(getContext(), "The loading mangas was interrupted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi nếu cần
                        Toast.makeText(getContext(), "The loading mangas was interrupted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (tag.equals("Bought")) {
            // Load danh sách manga đã mua
            if (currentUser != null) {
                String uid = currentUser.getUid();
                DatabaseReference purchasesRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid).child("HistoryPayment");

                purchasesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> mangaIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String mangaId = snapshot.child("ID_MANGA").getValue(String.class);
                            mangaIds.add(mangaId);
                        }

                        // Tạo một danh sách tạm thời để lưu trữ dữ liệu manga
                        List<Mangas> mangasList = new ArrayList<>();
                        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("Mangas");
                        for (String mangaId : mangaIds) {
                            mangaRef.orderByChild("ID_MANGA").equalTo(mangaId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot mangaSnapshot : dataSnapshot.getChildren()) {
                                                Mangas mangas = mangaSnapshot.getValue(Mangas.class);
                                                mangasList.add(mangas);
                                            }

                                            // Kiểm tra xem đã tải tất cả dữ liệu chưa
                                            if (mangasList.size() == mangaIds.size()) {
                                                listener.onDataLoaded(mangasList);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý lỗi nếu cần
                                            Toast.makeText(getContext(), "The loading mangas was interrupted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi nếu cần
                        Toast.makeText(getContext(), "The loading mangas was interrupted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            // Load tất cả manga (không có tag)
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Mangas> truyenTranhList = new ArrayList<>();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Mangas mangas = ds.getValue(Mangas.class);
                        if (category != null && mangas.getID_CATEGORY_MANGA().equals(category.getID_CATEGORY())) {
                            truyenTranhList.add(mangas);
                        }
                    }
                    // Gọi callback khi dữ liệu đã sẵn sàng
                    listener.onDataLoaded(truyenTranhList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "The loading mangas was interrupted",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
