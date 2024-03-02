package com.example.mangaplusapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaplusapp.Adapter.TruyenTranhAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.object.Category;
import com.example.mangaplusapp.object.TruyenTranh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashBoardMangaListFragment extends DialogFragment {
    private Category category;
    private View view;
    public DashBoardMangaListFragment() {}
    public DashBoardMangaListFragment (Category category){
        this.category = category;
    }
    public interface OnDataLoadedListener {
        void onDataLoaded(List<TruyenTranh> truyenTranhList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Khởi tạo adapter trước khi hiển thị dialog
        TruyenTranhAdapter adapter = new TruyenTranhAdapter(new ArrayList<>(), getContext());

        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dashboard_mangalist, null);
        RecyclerView recyclerView = view.findViewById(R.id.dashFragmentRcv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        builder
                .setView(view)
                        .setTitle(category.getNAME_CATEGORY())
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

        // Load dữ liệu và cập nhật adapter khi dữ liệu đã sẵn sàng
        loadMangas(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<TruyenTranh> truyenTranhList) {
                adapter.SetData(truyenTranhList);
                adapter.notifyDataSetChanged();
            }
        });

        return builder.create();
    }

    private void loadMangas(OnDataLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TruyenTranh> truyenTranhList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    TruyenTranh truyenTranh = ds.getValue(TruyenTranh.class);
                    if (truyenTranh != null && truyenTranh.getID_CATEGORY_MANGA() != null
                            && truyenTranh.getID_CATEGORY_MANGA().equals(category.getID_CATEGORY())) {
                        truyenTranhList.add(truyenTranh);
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
