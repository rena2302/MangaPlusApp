package com.example.mangaplusapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import Database.UserDatabase;

public class UserProfileFragment extends Fragment {
Button btnTest;
UserDatabase db;
TextView getUserNameInfoTxt,getUserNameTittleTxt,getUserEmailTxt,getUserPasswordTxt;
int userId;
String userEmail,userPassword,userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container,false);
        btnTest = root.findViewById(R.id.navToEditProfile);
        navigateLayout();
        //=========================================Get id=========================================//
        getUserNameInfoTxt = root.findViewById(R.id.userName_info);
        getUserNameTittleTxt = root.findViewById(R.id.userName_Tittle);
        getUserEmailTxt = root.findViewById(R.id.userEmail_info);
        getUserPasswordTxt = root.findViewById(R.id.userPassword_info);
        //****************************************************************************************//
        //=========================================Get data=======================================//
        db = new UserDatabase(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userId= preferences.getInt("user_id",-1);
        userName = db.getUserName(userId);
        userPassword = db.getUserPassword(userId);
        userEmail = db.getUserEmail(userId);
        //****************************************************************************************//
        //=========================================Get views======================================//
        if(userId!=-1){
            userExists();
        }
        else{
            userExists();
        }
        //****************************************************************************************//
        return root;
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
    void userExists(){
        getUserEmailTxt.setText(userEmail);
        getUserPasswordTxt.setText("chac kh de len dau");
        getUserNameInfoTxt.setText(userName);
        getUserNameTittleTxt.setText(userName);
    }
    void userNotExists(){
        getUserEmailTxt.setText("Guest");
        getUserPasswordTxt.setText("Guest");
        getUserNameInfoTxt.setText("Guest");
        // log out if want
    }
    void navigateLayout(){
        btnTest.setOnClickListener(v->{
          Intent intent = new Intent(getActivity(),UserEditActivity.class);
          startActivity(intent);
        });
    }
}