package com.example.mangaplusapp.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;

public class RegisterActivity extends AppCompatActivity {
    EditText userInputEmail;
    String userEmail;
    AppCompatButton signUpBtn;
    AppCompatButton haveAccount;
    ImageButton btnToLogin;
    UserDBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Ẩn KeyBoard
        View mainLayout = findViewById(R.id.ResEmailOverlay);
        KeyBoardHelper.ActionRemoveKeyBoardForActivity(mainLayout,RegisterActivity.this);
        //===============================Begin GET ID=============================================//
        haveAccount=findViewById(R.id.toLogin);
        btnToLogin = findViewById(R.id.backRegisterBtn);
        userInputEmail= findViewById(R.id.editTextEmail);
        signUpBtn = findViewById(R.id.registerBtn_act);
        //=================================END GET ID=============================================//
        //****************************************************************************************//
        //===================================Event Navigate layout================================//
        navigateLayout();
        //===================================Event Navigate layout================================//
        //****************************************************************************************//
        //===================================DATABASE=============================================//
        db= new UserDBHelper(this);
        db.open();
        //===================================DATABASE=============================================//
        //****************************************************************************************//
        //===================================BEGIN LOGIC REGISTER=================================//
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = userInputEmail.getText().toString();
                if(db.CheckEmailExists(userEmail)){
                    Toast.makeText(RegisterActivity.this,"This email is exists. Please Login or try new email !",Toast.LENGTH_SHORT).show();
                }
                else {
                    //==================================SUCCESS===================================//
                    if(db.validEmail(userEmail)){
                        Intent intent = new Intent(RegisterActivity.this,ForgotControlActivity.class);
                        intent.putExtra("EMAIL",userEmail);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"This email is not valid. Please Try again !",Toast.LENGTH_SHORT).show();
                    }
                    //==================================SUCCESS===================================//
                }
            }
        });
    }
    //======================================END LOGIC REGISTER====================================//
    void navigateLayout(){
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
}