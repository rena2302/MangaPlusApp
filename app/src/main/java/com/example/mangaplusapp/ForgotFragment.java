package com.example.mangaplusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import Database.CreateDatabase;

public class ForgotFragment extends Fragment {
    Button SendOtpBtn;
    String userEmail;
    CreateDatabase db;
    int userID;
    EditText getUserEmailTxt;
    public ForgotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_forgot, container, false);
        SendOtpBtn = root.findViewById(R.id.btnForgotSendOtp);
        //========================================GET ID==========================================//
        getUserEmailTxt=root.findViewById(R.id.forgotEmailUser_Input);
        //****************************************************************************************//
        //=====================================GET DATABASE=======================================//
        db=new CreateDatabase(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        userID=preferences.getInt("user_id",-1);
        userEmail=db.getUserEmail(userID);
        //****************************************************************************************//
        //==================================NAVIGATE LAYOUT=======================================//
        navigateLayout();
        //****************************************************************************************//
        //=======================================SEND OTP=========================================//

        //****************************************************************************************//
        return root;
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
    void navigateLayout(){
        //==================================BEGIN NAV TO VERIFICATION==================================//
        SendOtpBtn.setOnClickListener(e->{
            loadFragment(new VerificationFragment(),false);
        });
        //==================================END NAV TO VERIFICATION==================================//

    }
}