package com.example.mangaplusapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mangaplusapp.Helper.DBHelper.CategoryDBHelper;
import com.example.mangaplusapp.R;

public class SearchFragment extends Fragment {
    View view;
    CategoryDBHelper dbHelper;
    EditText userInputNameCateTxt;
    Button btnSubmit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        dbHelper = new CategoryDBHelper(getContext());
        userInputNameCateTxt = view.findViewById(R.id.inputCateTxt);
        btnSubmit = view.findViewById(R.id.btnSubmitCateAdd);
        btnSubmit.setOnClickListener(v->{
            String nameCate = userInputNameCateTxt.getText().toString();

            if(!dbHelper.insertData(nameCate)){
                Log.d("Error while insert cate","Error");
            }
        });
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}