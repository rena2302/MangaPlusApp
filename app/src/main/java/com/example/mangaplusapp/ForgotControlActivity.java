package com.example.mangaplusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        dbHelper = new UserDBHelper(this);
        String email = getIntent().getStringExtra("EMAIL");
        // use bundle when put and get data from activity into fragment
        editor.putString("user_email",email);
        editor.apply();
        Log.d("email", email);;
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