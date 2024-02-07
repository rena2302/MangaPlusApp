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

import java.util.Random;

import Database.CreateDatabase;
import papaya.in.sendmail.SendMail;

public class VerificationFragment extends Fragment {
    FirebaseAuth auth;
    String email,pasword;
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
        userID = preferences.getInt("user_id",-1);
        //****************************************************************************************//
        //=========================================SET DATA=======================================//
        email=preferences.getString("user_email","default@gmail.com");
        getEmailUserTxt.setText(email);
        Log.d("email's user", email);
        //****************************************************************************************//
        //=========================================SEND OTP=======================================//
        String keyOtp = generateOTP();
        Log.d("asd", keyOtp);
        sendOTPByEmail(keyOtp);
        //****************************************************************************************//
        //****************************************************************************************//
        submitOtp.setOnClickListener(v->{
            String otp1 = otp1Input.getText().toString();
            String otp2 = otp2Input.getText().toString();
            String otp3 = otp3Input.getText().toString();
            String otp4 = otp4Input.getText().toString();
            String otp= otp1+otp2+otp3+otp4;
            if(otp.equals(keyOtp)){
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getContext(),"Wrong OTP code", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    private String generateOTP() {
        int length = 4;
        String numbers = "0123456789"; //
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }
        return otp.toString();
    }

    private void sendOTPByEmail(String otp) {
        // sender pass is verification 2 case, got code pass app.
        // use connect https smtp email don't block by fire ware and private network
        SendMail mailTask = new SendMail("softwaretestact@gmail.com","lyzf yzcd mdyi yojp",email,"Manga Plus App's OTP ","Your OTP is -> "+otp);
        mailTask.execute();
    }

}