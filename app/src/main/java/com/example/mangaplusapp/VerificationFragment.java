package com.example.mangaplusapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import Helper.ActionHelper.KeyBoardHelper;
import Helper.DBHelper.UserDBHelper;
import Helper.LoadHelper.LoadFragment;
import Helper.ServiceHelper.OTP;

public class VerificationFragment extends Fragment{
    FirebaseAuth auth;
    String emailUser;
    ScriptGroup.Binding binding;
    TextView getEmailUserTxt,reSendOtp;
    UserDBHelper dbHelper;
    AppCompatButton submitOtp;
    OTP otpHelper;
    LoadFragment fragmentHelper;
    int userID;
    String keyOtp;

     EditText otp1Input,otp2Input,otp3Input,otp4Input;

    //Resend OTP time
    private int resendTime=60;
    //Lính canh sự kiện resend code
    private  boolean resendEnable= false;
    //Lính canh sự kiện đổi màu submit khi nhập đủ code
    private int selectedPosition=0;


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
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(requireContext(),container,inflater,R.layout.fragment_verification);
        //
        View root = inflater.inflate(R.layout.fragment_verification, container, false);
        //=========================================GET ID=========================================//
        getEmailUserTxt=root.findViewById(R.id.forgot_EmailUserTxt);
        otp1Input=root.findViewById(R.id.otp1);
        otp2Input=root.findViewById(R.id.otp2);
        otp3Input=root.findViewById(R.id.otp3);
        otp4Input=root.findViewById(R.id.otp4);


        submitOtp=root.findViewById(R.id.sendOtp);
        reSendOtp=root.findViewById(R.id.reSendOtpTxt);
        final TextView emailaddress=root.findViewById(R.id.forgot_EmailUserTxt);

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
        //Hiện email đang đăng kí hoặc forgot
        emailaddress.setText(emailUser);
        //****************************************************************************************//
        //=========================================SEND OTP=======================================//
        keyOtp = otpHelper.generateOTP();
        otpHelper.sendOTPByEmail(keyOtp,emailUser);
        Log.d("asd", keyOtp);
        Toast.makeText(getContext(),"Send OTP successfully",Toast.LENGTH_SHORT).show();
        //Sự kiện bắt đầu chạy timer resend OTP
        //Chạy lần đầu
        startCountDownTimer();
        //****************************************************************************************//
        //****************************************************************************************//

        //Hiện bàn phím sau khi khởi tạo cùng với fragment sẽ mặc định focus vào otp1Input
        showkeyboard(otp1Input);

        //Sự kiện lắng nghe thay đổi trong Edittext
        TextWatcher textWatcher=new TextWatcher( ) {
            String otp1 = otp1Input.getText().toString();
            String otp2 = otp2Input.getText().toString();
            String otp3 = otp3Input.getText().toString();
            String otp4 = otp4Input.getText().toString();
            String otp= otp1+otp2+otp3+otp4;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Xử lý trước khi văn bản thay đổi
                Log.d("Beforeactive", String.valueOf(selectedPosition));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Xử lý trong quá trình văn bản đang thay đổi
                Log.d("Onactive", String.valueOf(selectedPosition));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Xử lý sau khi văn bản đã thay đổi
                if(s.length()>0)
                {
                    if(selectedPosition==0||otp1Input.isFocused())
                    {
                        selectedPosition=0;
                        ListenNullText(selectedPosition,otp1Input,otp2Input,otp3Input,otp4Input);
                    }
                    else if (selectedPosition==1||otp2Input.isFocused())
                    {
                        selectedPosition=1;
                        ListenNullText(selectedPosition,otp1Input,otp2Input,otp3Input,otp4Input);
                    }
                    else if (selectedPosition==2||otp3Input.isFocused())
                    {
                        selectedPosition=2;
                        ListenNullText(selectedPosition,otp1Input,otp2Input,otp3Input,otp4Input);
                    }
                    else if (selectedPosition==3||otp4Input.isFocused())
                    {
                        selectedPosition=3;
                        ListenNullText(selectedPosition,otp1Input,otp2Input,otp3Input,otp4Input);
                    }
                }
            }
        };
        //Lắng nghe thay đổi trong từng edittext
        otp1Input.addTextChangedListener(textWatcher);
        otp2Input.addTextChangedListener(textWatcher);
        otp3Input.addTextChangedListener(textWatcher);
        otp4Input.addTextChangedListener(textWatcher);

        //Sự kiện lắng nghe delete
        otp1Input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    submitOtp.setBackgroundResource(R.drawable.btn_defalt);
                    selectedPosition=0;
                    showkeyboard(otp1Input);
                }
                return false;
            }
        });
        otp2Input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable editable2=otp2Input.getText();
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    submitOtp.setBackgroundResource(R.drawable.btn_defalt);
                    if(editable2.length()<=0)
                    {
                        selectedPosition=0;
                        showkeyboard(otp1Input);
                    }
                    else
                    {
                        selectedPosition=1;
                        showkeyboard(otp2Input);
                    }
                }
                return false;
            }
        });
        otp3Input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable editable3=otp3Input.getText();
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    submitOtp.setBackgroundResource(R.drawable.btn_defalt);
                    if(editable3.length()<=0)
                    {
                        selectedPosition=1;
                        showkeyboard(otp2Input);
                    }
                    else
                    {
                        selectedPosition=2;
                        showkeyboard(otp3Input);
                    }
                }
                return false;
            }
        });
        otp4Input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable editable4=otp4Input.getText();
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    submitOtp.setBackgroundResource(R.drawable.btn_defalt);
                    if(editable4.length()<=0)
                    {
                        selectedPosition=2;
                        showkeyboard(otp3Input);
                    }
                    else
                    {
                        selectedPosition=3;
                        showkeyboard(otp4Input);
                    }
                }
                return false;
            }
        });

//        otpHelper.sendOTPByEmail(keyOtp,emailUser);
        reSendOtp.setOnClickListener(v->{
            if(resendEnable)
            {
                keyOtp = otpHelper.generateOTP();
                otpHelper.sendOTPByEmail(keyOtp,emailUser);
                Log.d("asd", keyOtp);
                startCountDownTimer();
            }
//            ReSendOTPByTime(emailUser,30000);
        });

        submitOtp.setOnClickListener(v->{
            String otp1 = otp1Input.getText().toString();
            String otp2 = otp2Input.getText().toString();
            String otp3 = otp3Input.getText().toString();
            String otp4 = otp4Input.getText().toString();
            String otp= otp1+otp2+otp3+otp4;
            Log.d("keyOTPtest", keyOtp);
            if(otp.length()==4)
            {
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
            }
            else
            {
                Toast.makeText(getContext(),"Not entering enough OTP code", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public int ListenNullText(int SelectedPosition,EditText input1,EditText input2, EditText input3, EditText input4)
    {
        Editable editable1=input1.getText();
        Editable editable2=input2.getText();
        Editable editable3=input3.getText();
        Editable editable4=input4.getText();
        if (editable1.length()<=0)
        {
           SelectedPosition=0;
           showkeyboard(input1);
        }
        else if(editable2.length()<=0)
        {
            SelectedPosition =1;
            showkeyboard(input2);
        }
        else if (editable3.length()<=0)
        {
            SelectedPosition=2;
            showkeyboard(input3);
        }
        else if (editable4.length()<=0)
        {
            SelectedPosition=3;
            showkeyboard(input4);
        }
        else
        {
            submitOtp.setBackgroundResource(R.drawable.btn_accept);
        }
        return  SelectedPosition;
    }

//    public void ReSendOTPByTime(String userEmail,int Time){
//        new CountDownTimer(Time, 1000) {
//            public void onTick(long millisUntilFinished) {
//                reSendOtp.setText("Resend OTP success full ! seconds remaining: " + millisUntilFinished / 1000);
//                //here you can have your logic to set text to edittext
//            }
//            public void onFinish() {
//                reSendOtp.setText("Didn't receive the OTP? Resend OTP");
//                String otp =  otpHelper.generateOTP();
//                otpHelper.sendOTPByEmail(otp,userEmail);
//            }
//        }.start();
//    }

    //hiển thị bàn phím và focus cho từng edittext
    private  void showkeyboard(EditText otpET)
    {
        otpET.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //Hiện bàn phím không cần tương tác vào edittext
        inputMethodManager.showSoftInput(otpET,InputMethodManager.SHOW_IMPLICIT);
    }
    private void startCountDownTimer()
    {
        //nếu resendEnable=false thì bắt dầu đém ngược
        resendEnable=false;
        reSendOtp.setTextColor(Color.parseColor("#99000000"));
        //thời gian đếm ngược 60x1000 và mõi lần đếm ngược là 1s
        new CountDownTimer(resendTime *1000,1000)
        {
            //cập nhật thời gian đếm ngược sau mỗi lần đơn vị thời gian giảm
            @Override
            public void onTick(long millisUntilFinished) {
                reSendOtp.setText((String.valueOf(millisUntilFinished/1000))+"s");
            }
            //Khi kết thúc đổi giá trị thành true và dổi màu text
            @Override
            public void onFinish() {
                resendEnable=true;
                keyOtp = otpHelper.generateOTP();
                reSendOtp.setText("Resend Code");
                reSendOtp.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }
}