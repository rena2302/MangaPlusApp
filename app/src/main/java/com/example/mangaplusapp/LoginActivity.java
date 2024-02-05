package com.example.mangaplusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import Database.CreateDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt;
    TextView forgotPasswordTxt,toSignUpTxt;
    Button btnLoginTxt;
    CreateDatabase db;
    int idUser;

    //Create sign in Google
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    //End sign in Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //===============================Begin get id for login basic=============================//
        toSignUpTxt = findViewById(R.id.toSignUp);
        emailTxt = (EditText) findViewById(R.id.editTextEmail);
        passwordTxt = (EditText) findViewById(R.id.editTextPassword);
        btnLoginTxt = (Button) findViewById(R.id.loginBtn_act);
        forgotPasswordTxt = (TextView) findViewById(R.id.forgotPassword_act);
        //================================End get id for login basic==============================//
        //****************************************************************************************//
        //================================Begin get data for login basic==========================//
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //================================END get data for login basic============================//
        //****************************************************************************************//
        //===============================Begin get id for login with social=======================//
        googleBtn = findViewById(R.id.googleBtn_login);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso); // activity va options
        //===============================End get id for login with social=========================//
        //****************************************************************************************//
        //===============================CONNECT DATABASE=========================================//
        db = new CreateDatabase(this);
        db.open();
        //=============================== End Connect DataBase====================================//
        //****************************************************************************************//
        //=============================== BEGIN NAVIGATE LAYOUT===================================//
        navigateLayout();
        //=============================== END NAVIGATE LAYOUT===================================//
        //****************************************************************************************//
        //===============================Begin Connect Login With Social==========================//
                                        // Process IS EMPTY //
        //===============================End Connect Login With Social============================//
        //****************************************************************************************//
        //===============================BEGIN LOGIC LOGIN BASIC==================================//
        btnLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            // this event final
            public void onClick(View v) {
                String userEmail = emailTxt.getText().toString();
                String userPassword = passwordTxt.getText().toString();
                // if user do nothing or Missing input
                if(userEmail.equals("")||userPassword.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkEmailPass= db.CheckEmailPassword(userEmail,userPassword);
                    // if email and password valid -> nav to home activity
                    if(checkEmailPass){
        ////////===========================Begin Login Successful=========================//////////
                        idUser=db.loginUser(userEmail,userPassword);
                        editor.putInt("user_id",idUser);
                        editor.putString("user_email", userEmail);
                        editor.apply();
                        Toast.makeText(LoginActivity.this,"Sign Ip Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
        ////////===========================END Login Successful=========================////////////
                    }
                    // show message input error
                    else{
                        Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //===============================END LOGIC LOGIN BASIC====================================//
        //****************************************************************************************//
        //===============================BEGIN FORGOT PASSWORD====================================//
//        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userEmail = emailTxt.getText().toString();
//
//                String oldPassword = passwordTxt.getText().toString();
//                String newPassword = passwordTxt.getText().toString();
//                String confirmNewPassword=passwordTxt.getText().toString();
//
//                Boolean checkEmail = db.CheckEmailExists(userEmail);
//                Boolean checkPassword = db.CheckPassword(oldPassword);
//
//                if(oldPassword.equals("")||newPassword.equals("")||confirmNewPassword.equals("")||userEmail.equals("")){
//                    Toast.makeText(LoginActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if(checkEmail){
//                        if(checkPassword){
//                            if(newPassword.equals(confirmNewPassword)){
//                                boolean resetPass = db.resetPassword(newPassword);
//                                if(resetPass){
//                                    Toast.makeText(LoginActivity.this,"Password updated", Toast.LENGTH_SHORT).show();
//                                    // nav to login
//                                }
//                               else{
//                                    Toast.makeText(LoginActivity.this,"Error some thing in database", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else{
//                                Toast.makeText(LoginActivity.this,"New Password and Confirm Password not match", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this,"Password not match in database", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(LoginActivity.this,"Can not find your email account, try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        //===============================END FORGOT PASSWORD======================================//
        //****************************************************************************************//
        //===============================Begin login with social==================================//
        GoogleSignInAccount accountGoogle = GoogleSignIn.getLastSignedInAccount(this);
        if(accountGoogle!=null){
            navToSuccess();
            // is mean after user login with social, if user not sign out, but user out process or program
            // this code will help user don't sign in again, when user log this app, they will go to main activity
        }
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithSocial();
            }
        });
    }
    void navigateLayout(){
        //==================================BEGIN NAV TO SIGN UP==================================//
        toSignUpTxt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // bat su kien
            startActivity(intent); // chay su kien
        });
        //==================================END NAV TO SIGN UP==================================//
    }

    void navToSuccess(){
        finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
    void signInWithSocial(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==3000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{ // when sign in successfully, will active process to main activity
                task.getResult(ApiException.class);
                Toast.makeText(LoginActivity.this,"Sign Ip Successfully", Toast.LENGTH_SHORT).show();
                navToSuccess();
            }
            catch (ApiException e){
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

}