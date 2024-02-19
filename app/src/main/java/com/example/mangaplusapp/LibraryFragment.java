package com.example.mangaplusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import Database.UserDatabase;

public class LibraryFragment extends Fragment {
    UserDatabase db;
    TextView userEmailTxt,userNameTxt;
    Button userLogOutBtn;
    ImageButton testlog,testlogmanga;
    int userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        testlog = rootView.findViewById(R.id.btnNavToProfile); // sau nay xoa
        testlogmanga = rootView.findViewById(R.id.btnNavToTestAddManga);// sau nay xoa
        ///////===========================Get data=========================/////////////////////////
        db = new UserDatabase(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        userId=preferences.getInt("user_id",-1);
        String userEmail = db.getUserEmail(userId);
        String userName = db.getUserName(userId);
        ////////===========================Begin Status Logged=========================/////////////
        if(isLoggedIn()){
            navigateLayout();
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
    void navigateLayout(){
        testlog.setOnClickListener(v->{
            loadFragment(new UserProfileFragment(),false);
        });
        testlogmanga.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(),TestAddMangaActivity.class);
            startActivity(intent);
        });
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

}