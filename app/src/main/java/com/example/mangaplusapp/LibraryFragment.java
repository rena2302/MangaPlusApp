package com.example.mangaplusapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Database.CreateDatabase;

public class LibraryFragment extends Fragment {
    CreateDatabase db;
    TextView userEmailTxt,userNameTxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        // Initialize views after inflating the layout
        userNameTxt = rootView.findViewById(R.id.userName_info);
        userEmailTxt = rootView.findViewById(R.id.userEmail_info);

        // get database
        db = new CreateDatabase(requireContext());

        // get data
        String userEmail = db.getUserEmail();
        userEmailTxt.setText(userEmail);

        return rootView;
    }
}