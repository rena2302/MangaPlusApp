package com.example.mangaplusapp.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mangaplusapp.Adapter.ChapterAdapter;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.object.Chapters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChapterListFragment extends DialogFragment {
    View view;
    String mangaId;
    private boolean isPdfLoaded = false;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Chapters> chaptersList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        assert args != null;
        mangaId = args.getString("ID_MANGA_CHAPTER");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ChapterAdapter adapter = new ChapterAdapter(new ArrayList<>(), getContext(), isPdfLoaded, fragmentManager,this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_chapter_list, null);

        RecyclerView recyclerView = view.findViewById(R.id.chapterList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Chapter Select")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        loadChapters(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Chapters> chaptersList) {
                adapter.setData(chaptersList);
                adapter.notifyDataSetChanged();
            }
        });

        return builder.create();
    }
    public void setPdfLoaded(boolean pdfLoaded) {
        this.isPdfLoaded = pdfLoaded;
    }
    private void loadChapters(OnDataLoadedListener listener) {
        List<Chapters> chaptersList = new ArrayList<>();
        //Get all data from firebase > Categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chapters");
        reference.orderByChild("ID_MANGA_CHAPTER").equalTo(mangaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chaptersList.clear();
                int categoryCount = (int) snapshot.getChildrenCount();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    Chapters chapters = ds.getValue(Chapters.class);
                    //add to List
                    chaptersList.add(chapters);
                }
                listener.onDataLoaded(chaptersList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}