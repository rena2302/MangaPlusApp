package com.example.mangaplusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;

import Database.CreateDatabase;

public class LibraryFragment extends Fragment {
    CreateDatabase db;
    TextView userEmailTxt,userNameTxt;
    Button userLogOutBtn;
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
        ///////===========================Get ID from layout Fragment =========================/////
        userNameTxt = rootView.findViewById(R.id.userName_info);
        userEmailTxt = rootView.findViewById(R.id.userEmail_info);
        userLogOutBtn=rootView.findViewById(R.id.userLogOut_info);
        ///////===========================Get data=========================/////////////////////////
        db = new CreateDatabase(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String userEmail = db.getUserEmail();
        String userName = db.getUserName();
        ////////===========================Begin Status Logged=========================/////////////
        if(isLoggedIn()){
            userEmailTxt.setText(userEmail);
            userNameTxt.setText(userName);
            userLogOutBtn.setHint("Log Out");
        ///////===========================Begin Logout=========================/////////////////////
            userLogOutBtn.setOnClickListener(v -> {
            // Clear session and navigate to login activity
                try {
                    clearSession();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Log out successful", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Something was wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            handleNotLoggedIn();
            userLogOutBtn.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            });
        ////////===========================End Logout=========================//////////////////////
        }
        ////////===========================End Status Logged=========================///////////////
        //****************************************************************************************//
        return rootView;
    }
    private boolean isLoggedIn(){
        return db.isUserLoggedIn();
    }
    private void handleNotLoggedIn() {
        userNameTxt.setText("Guest");
        userEmailTxt.setText("Not logged in");
        userLogOutBtn.setHint("Login");
    }
    private void clearSession() {
        db.clearUserSession();
    }


}