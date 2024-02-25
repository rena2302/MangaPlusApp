package com.example.mangaplusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import Helper.ActionHelper.KeyBoardHelper;
import Helper.DBHelper.UserDBHelper;
import Helper.LoadHelper.LoadFragment;
import Helper.ServiceHelper.OTP;

public class VerificationFragment extends Fragment {
    FirebaseAuth auth;
    String emailUser;
    ScriptGroup.Binding binding;
    TextView getEmailUserTxt,reSendOtp;
    UserDBHelper dbHelper;
    AppCompatButton submitOtp;
    OTP otpHelper;
    LoadFragment fragmentHelper;
    int userID;
    //Biến theo dõi sự kiện Ontouch
    private View.OnTouchListener touchListener;

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
        //Ẩn keyboard
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(requireContext(),container,inflater,R.layout.fragment_verification,touchListener);
        View root = inflater.inflate(R.layout.fragment_verification, container, false);
        //=========================================GET ID=========================================//
        getEmailUserTxt=root.findViewById(R.id.forgot_EmailUserTxt);
        EditText otp1Input,otp2Input,otp3Input,otp4Input;
        otp1Input=root.findViewById(R.id.otp1);
        otp2Input=root.findViewById(R.id.otp2);
        otp3Input=root.findViewById(R.id.otp3);
        otp4Input=root.findViewById(R.id.otp4);
        submitOtp=root.findViewById(R.id.sendOtp);
        reSendOtp=root.findViewById(R.id.reSendOtpTxt);
        //****************************************************************************************//
        //=========================================GET DATA=======================================//
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        otpHelper = new OTP();
        dbHelper=new UserDBHelper(getContext());
        auth=FirebaseAuth.getInstance();
        //****************************************************************************************//
        //=========================================GET HELPER=======================================//
        //****************************************************************************************//
        //=========================================SET DATA=======================================//
        emailUser = preferences.getString("user_email",null);
        //****************************************************************************************//
        //=========================================SEND OTP=======================================//
        String keyOtp = otpHelper.generateOTP();
        Log.d("asd", keyOtp);
        Toast.makeText(getContext(),"Send OTP successfully",Toast.LENGTH_SHORT).show();
        otpHelper.sendOTPByEmail(keyOtp,emailUser);
        reSendOtp.setOnClickListener(v->{
            ReSendOTPByTime(emailUser,30000);
        });
        //****************************************************************************************//
        //****************************************************************************************//
        submitOtp.setOnClickListener(v->{
            String otp1 = otp1Input.getText().toString();
            String otp2 = otp2Input.getText().toString();
            String otp3 = otp3Input.getText().toString();
            String otp4 = otp4Input.getText().toString();
            String otp= otp1+otp2+otp3+otp4;
            if(otp.equals(keyOtp)){
                //===============================Case forgot password=============================//
                if(dbHelper.CheckEmailExists(emailUser)){
                    // khi có figma thì cho chạy vào form edit password
                    userID =dbHelper.loginUser(emailUser);
                    editor.putString("user_email", emailUser);
                    editor.putInt("user_id",userID); // put user id
                    editor.apply();
                    fragmentHelper = new LoadFragment();
                    fragmentHelper.loadFragment(getParentFragmentManager(),new CreatePasswordFragment(),false,R.id.forgotContainer);
                }
                //===============================Case Register ===================================//
                else{
                    userID =dbHelper.loginUser(emailUser);
                    editor.putString("user_email", emailUser);
                    editor.apply();
                    // nav to new password and confirm password and insert data into database -> nav to login
                    fragmentHelper = new LoadFragment();
                    fragmentHelper.loadFragment(getParentFragmentManager(),new CreatePasswordFragment(),false,R.id.forgotContainer);
                }
            }
            else{
                Toast.makeText(getContext(),"Wrong OTP code", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    public void ReSendOTPByTime(String userEmail,int Time){
        new CountDownTimer(Time, 1000) {
            public void onTick(long millisUntilFinished) {
                reSendOtp.setText("Resend OTP success full ! seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                reSendOtp.setText("Didn't receive the OTP? Resend OTP");
                String otp =  otpHelper.generateOTP();
                otpHelper.sendOTPByEmail(otp,userEmail);
            }
        }.start();
    }

}