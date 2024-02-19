package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Database.UserDatabase;

public class UserEditActivity extends AppCompatActivity {
    EditText getUserNameTxt,getUserNewPasswordTxt,getUserEmailTxt,getUserCfPasswordTxt,getUserOldPasswordTxt;
    UserDatabase db;
    Button btnSubmit;
    String userEmail,userName,userPassword;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        //===================================DATABASE=============================================//
        db =new UserDatabase(this);
        db.open();
        SharedPreferences preferences = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        userID= preferences.getInt("user_id",-1);
        //****************************************************************************************//
        //===================================GET ID===============================================//
        getUserNameTxt=findViewById(R.id.editNameTxt_info);
        getUserEmailTxt=findViewById(R.id.editEmailTxt_info);
        getUserNewPasswordTxt=findViewById(R.id.editNewPasswordTxt_info);
        getUserCfPasswordTxt=findViewById(R.id.editCfPasswordTxt_info);
        getUserOldPasswordTxt = findViewById(R.id.editPasswordOldTxt_info);
        btnSubmit=findViewById(R.id.btnCfEdit_info);
        //****************************************************************************************//
        //===================================GET DATA===============================================//
        userEmail = db.getUserEmail(userID);
//        int userId = db.getUserIDByEmail(userEmail);
        userName= db.getUserName(userID);
        userPassword = db.getUserPassword(userID);
        //****************************************************************************************//
        if(isLogged()){
            handleLoggedIn();
            ValidDataInput();
        }
        else{
            handleNotLoggedIn();
        }

    }
    private void ValidDataInput(){
        btnSubmit.setOnClickListener(v->{
            String userInputName=  getUserNameTxt.getText().toString();
            String userInputEmail= getUserEmailTxt.getText().toString();
            String userInputNewPassword = getUserNewPasswordTxt.getText().toString();
            String userInputOldPassword = getUserOldPasswordTxt.getText().toString();
            String userInputCfPassword = getUserCfPasswordTxt.getText().toString();
            if(userInputEmail.equals("")||userInputNewPassword.equals("")||userInputOldPassword.equals("")||userInputCfPassword.equals("")){
                Toast.makeText(UserEditActivity.this, "Please enter all the field", Toast.LENGTH_SHORT).show();
            }
            else{
                if(userInputOldPassword.equals(userPassword)){
                    if(db.validEmail(userInputEmail)||db.validPassword(userInputNewPassword)){
                        if(userInputNewPassword.equals(userInputCfPassword)){
                            //===========================SUCCESS==================================//
                            try{
                                db.insertData(userInputEmail,userInputNewPassword,userInputName);
                                // if insert success will nav to User profile Fragment and show messages
                                Toast.makeText(UserEditActivity.this, "Update info successfully", Toast.LENGTH_SHORT).show();
                                loadFragment(new UserProfileFragment(),false);
                            }
                            catch(Exception e){
                                Log.e(null, "Something was wrong when insert data in ValidDataInput in UserEditActivity",e);
                            }

                            //===========================SUCCESS==================================//
                        }
                        else{
                            Toast.makeText(UserEditActivity.this, "Password and confirm password not matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(UserEditActivity.this, "Password or Email is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(UserEditActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isLogged(){
      return  db.isUserLoggedIn();
    }
    private void clearSession() {
        db.clearUserSession();
    }
    private void handleNotLoggedIn(){
        getUserEmailTxt.setHint("Guest");
        getUserNameTxt.setHint("Guest");
        getUserOldPasswordTxt.setHint("Guest");
    }
    private void handleLoggedIn(){
        getUserEmailTxt.setHint(userEmail);
        getUserNameTxt.setHint(userName);
        getUserOldPasswordTxt.setHint(userPassword);
    }
    void navigateLayout(){

    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
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