package com.example.mangaplusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import Database.CreateDatabase;
import Helper.ServiceHelper.OTP;

public class VerificationFragment extends Fragment {
    FirebaseAuth auth;
    String emailUser,pasword;
    ScriptGroup.Binding binding;
    TextView getEmailUserTxt;
    CreateDatabase db;
    Button submitOtp;
    int userID;

    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_verification, container, false);
        //=========================================GET ID=========================================//
        getEmailUserTxt=root.findViewById(R.id.forgot_EmailUserTxt);
        EditText otp1Input,otp2Input,otp3Input,otp4Input;
        otp1Input=root.findViewById(R.id.otp1);
        otp2Input=root.findViewById(R.id.otp2);
        otp3Input=root.findViewById(R.id.otp3);
        otp4Input=root.findViewById(R.id.otp4);
        submitOtp=root.findViewById(R.id.sendOtp);
        //****************************************************************************************//
        //=========================================GET DATA=======================================//
        db=new CreateDatabase(getContext());
        auth=FirebaseAuth.getInstance();
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();

        //****************************************************************************************//
        //=========================================GET HELPER=======================================//
        OTP otpHelper = new OTP();
        //****************************************************************************************//
        //=========================================SET DATA=======================================//
        emailUser=preferences.getString("user_email","default@gmail.com");
        getEmailUserTxt.setText(emailUser); // set text for View
        Log.d("email's user", emailUser);
        //****************************************************************************************//
        //=========================================SEND OTP=======================================//
        String keyOtp = otpHelper.generateOTP();
        Log.d("asd", keyOtp);
        otpHelper.sendOTPByEmail(keyOtp,emailUser);
        //****************************************************************************************//
        //****************************************************************************************//
        submitOtp.setOnClickListener(v->{
            String otp1 = otp1Input.getText().toString();
            String otp2 = otp2Input.getText().toString();
            String otp3 = otp3Input.getText().toString();
            String otp4 = otp4Input.getText().toString();
            String otp= otp1+otp2+otp3+otp4;
            if(otp.equals(keyOtp)){
                // khi có figma thì cho chạy vào form edit password
                userID =db.loginUser(emailUser);
                editor.putString("user_email", emailUser);
                editor.putInt("user_id",userID); // put user id
                editor.apply();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getContext(),"Wrong OTP code", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

}