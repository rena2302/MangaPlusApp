package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.CreateDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btnLogin;
    CreateDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lay id
        setContentView(R.layout.activity_login);
        TextView toSignUp = findViewById(R.id.toSignUp);
        //lay id
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // bat su kien
                startActivity(intent); // chay su kien
            }
        });

        //database
        // CONNECT DATABASE
        db = new CreateDatabase(this);
        db.open();
        //BEGIN LOGIC LOGIN
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.loginBtn_act);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                // if user do nothing or Missing input
                if(userEmail.equals("")||userPassword.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkEmailPass= db.CheckEmailPassword(userEmail,userPassword);
                    // neu email va password hop le -> ra home activity
                    if(checkEmailPass){

                        Toast.makeText(LoginActivity.this,"Sign Ip Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    // thong bao cac truong khong hop le
                    else{
                        Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}