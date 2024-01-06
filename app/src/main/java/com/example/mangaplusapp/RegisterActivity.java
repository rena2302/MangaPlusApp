package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Database.CreateDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password,repassword;
    Button signUp;
    CreateDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // LAY ID
        setContentView(R.layout.activity_register);
        TextView toLogin=findViewById(R.id.toLogin);
        ImageButton btnToLogin = findViewById(R.id.backRegisterBtn);
        // LAY ID
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CHUYEN LAYOUT
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        // CONNECT DATABASE
        db= new CreateDatabase(this);
        db.open();
        //BEGIN LOGIC REGISTER
        name=(EditText) findViewById(R.id.editTextName);
        email= (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        repassword = (EditText) findViewById(R.id.editTextCfPassword);
        signUp = (Button) findViewById(R.id.registerBtn_act);
        db= new CreateDatabase(this);

        // su kien dang ky
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userCfPassword = repassword.getText().toString();

                // neu may thg input == null
                if(userEmail.equals("")|| userPassword.equals("") || userName.equals("") || userCfPassword.equals("")){
                    Toast.makeText(RegisterActivity.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();

                }
                else{
                    // neu pass match voi cf pass
                    if(userPassword.equals(userCfPassword)){
                        Boolean checkEmail = db.CheckEmail(userEmail);
                        if(checkEmail==false){ // neu email khong ton tai trong he thong
                            Boolean insert = db.insertData(userEmail,userPassword);

                            if(insert) {
                                // thong bao thanh cong
                                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                // chuyen sang home layout
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }
                            // neu insert thanh cong
                            else{
                                // thong bao that bai
                                Toast.makeText(RegisterActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                                // do nothing
                            }
                        }
                        else{
                            // thong bao da co email trong he thong, vui long dang nhap
                            Toast.makeText(RegisterActivity.this, "User already exists ! Please sign in", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Password not match, please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}