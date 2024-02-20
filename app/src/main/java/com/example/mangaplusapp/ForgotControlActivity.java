package com.example.mangaplusapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import Helper.DBHelper.UserDBHelper;

public class ForgotControlActivity extends AppCompatActivity {
    UserDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_control);
        String email = getIntent().getStringExtra("EMAIL");
        if(dbHelper.CheckEmailExists(email)){
            //======================================Nav to ForgotFragment=========================//
            loadFragment(new ForgotFragment(),false);
            //************************************************************************************//
        }
        else{
            loadFragment(new VerificationFragment(),false);
        }



    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
}