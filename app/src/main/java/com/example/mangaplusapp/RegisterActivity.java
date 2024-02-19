package com.example.mangaplusapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.UserDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText nameTxt,emailTxt,passwordTxt,repasswordTxt;
    String userInputEmail,userInputPassword,userInputCfPassword;
    Button signUpTxt;
    TextView haveAccount;
    ImageButton btnToLogin;
    UserDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //===============================Begin GET ID=============================================//
        haveAccount=findViewById(R.id.toLogin);
        btnToLogin = findViewById(R.id.backRegisterBtn);
        emailTxt= findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);
        repasswordTxt = findViewById(R.id.editTextCfPassword);
        signUpTxt = findViewById(R.id.registerBtn_act);
        //=================================END GET ID=============================================//
        //****************************************************************************************//
        //===================================Event Navigate layout================================//
        navigateLayout();
        //===================================Event Navigate layout================================//
        //****************************************************************************************//
        //===================================DATABASE=============================================//
        db= new UserDatabase(this);
        db.open();
        //===================================DATABASE=============================================//
        //****************************************************************************************//
        //===================================BEGIN LOGIC REGISTER=================================//
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userInputEmail = emailTxt.getText().toString();
                 userInputPassword = passwordTxt.getText().toString();
                 userInputCfPassword = repasswordTxt.getText().toString();
                // if user input email,pass.....  null
                if(userInputEmail.equals("")|| userInputPassword.equals("")|| userInputCfPassword.equals("")){
                    Toast.makeText(RegisterActivity.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    //if pass < 8 length
                    if(!db.validPassword(userInputPassword)){
                        Toast.makeText(RegisterActivity.this, "Please enter greater than 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // if pass match cf pass
                        if(userInputPassword.equals(userInputCfPassword)){
                            Boolean checkEmail = db.CheckEmailExists(userInputEmail);
                            // if regex email valid
                            if(!db.validEmail(userInputEmail)){
                                Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(!checkEmail){ // if email not existed in database
                                    Boolean insert = db.insertData(userInputEmail,userInputPassword,null);
                                    if(insert) {
                                        //======================SUCCESS===========================//
                                        // show message success full
                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        // nav to home layout
                                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                        startActivity(intent);
                                        //======================SUCCESS===========================//
                                    }
                                    else{
                                        // show message not success full
                                        Toast.makeText(RegisterActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                                        // do nothing
                                    }
                                }
                                else{
                                    // message email existed in database,please sign in
                                    Toast.makeText(RegisterActivity.this, "User already exists ! Please sign in", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Password not match, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    //======================================END LOGIC REGISTER====================================//
    void navigateLayout(){
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void testOTP(int userId){
        signUpTxt.setOnClickListener(v->{
            userInputEmail = emailTxt.getText().toString();
            if(db.validEmail(userInputEmail)){
                if(db.CheckEmailExists(userInputEmail)){
                    // nav to login
                }
                else{
                    // SEND OTP
                }
            }
            else{
                // not valid email
            }
        });
    }

}