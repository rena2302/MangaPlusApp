package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.mangaplusapp.Activity.LoginActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;

public class CreatePasswordFragment extends Fragment {

    EditText getUserNameTxt,getUserPasswordTxt,getUserRePasswordTxt;
    String userName_register,userEmail,userPassword,userRePassword;
    UserDBHelper dbHelper;
    AppCompatButton btnSubmit;
    RelativeLayout layoutInput;
    RelativeLayout layoutInputUserName;
    int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Ẩn keyboard
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(requireContext(),container,inflater, R.layout.fragment_create_password);
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_create_password, container, false);
        dbHelper= new UserDBHelper(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userId=preferences.getInt("user_id",-1);
        if(userId==-1)
        {
            Log.d("sss", "null-1 ");
        }
        else
        {
            Log.d("aaa", String.valueOf(userId));
        }
        layoutInputUserName=root.findViewById(R.id.InputUNCP);
        btnSubmit = root.findViewById(R.id.btnSubmitInfo);
        getUserNameTxt = root.findViewById(R.id.userNewNameTxt);
        getUserPasswordTxt = root.findViewById(R.id.userNewPasswordTxt);
        getUserRePasswordTxt = root.findViewById(R.id.userRePasswordTxt);
        layoutInput=root.findViewById(R.id.userInputInfo_layout);
        userEmail =preferences.getString("user_email",null);

        //===================================ForgotPassword Case==================================//
        if(dbHelper.CheckEmailExists(userEmail)){
            layoutInput.removeView(layoutInputUserName);
            btnSubmit.setOnClickListener(v->{
                userPassword=getUserPasswordTxt.getText().toString();
                userRePassword = getUserRePasswordTxt.getText().toString();
                if(userPassword.equals(" ")|| userRePassword.equals(" ")){
                    Toast.makeText(getContext(),"Password and Confirm Password not matches", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dbHelper.validPassword(userPassword)){
                        //=================================SUCCESSFUL=============================//
                        Log.d("user_email", userEmail);
                        Log.d("user_password", userPassword);
                        Log.d("user_rePassword", userRePassword);

                        dbHelper.UpdatePassword(userId,userPassword.trim());
                        Toast.makeText(getContext(),"RePassword Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        //=================================SUCCESSFUL=============================//
                    }
                    else if(userPassword.length() <8){
                        Toast.makeText(getContext(),"Please enter password length >= 8", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Please enter password valid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //===================================New Account Case=====================================//
        else{
            btnSubmit.setOnClickListener(v->{
                userName_register = getUserNameTxt.getText().toString();
                userPassword=getUserPasswordTxt.getText().toString();
                userRePassword = getUserRePasswordTxt.getText().toString();
                if(userPassword.equals(" ")|| userRePassword.equals(" ")){
                    Toast.makeText(getContext(),"Password and Confirm Password not matches", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dbHelper.validPassword(userPassword)){
                        //=================================SUCCESSFUL=============================//
                        dbHelper.insertData(userEmail,userPassword,userName_register);
                        Log.d("user_email", userEmail);
                        Log.d("user_password", userPassword);
                        Log.d("user_rePassword", userRePassword);
                        Log.d("user_name", userName_register);
                        Toast.makeText(getContext(),"Register Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),LoginActivity.class);
                        startActivity(intent);
                        //=================================SUCCESSFUL=============================//
                    }
                    else if(userPassword.length() <8){
                        Toast.makeText(getContext(),"Please enter password length >= 8", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Please enter password valid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return root;

    }
}