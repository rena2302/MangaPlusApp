package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

import Database.CreateDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText nameTxt,emailTxt,passwordTxt,repasswordTxt;
    Button signUpTxt;
    TextView haveAccount;
    ImageButton btnToLogin;
    CreateDatabase db;
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
        db= new CreateDatabase(this);
        db.open();
        //===================================DATABASE=============================================//
        //****************************************************************************************//
        //===================================BEGIN LOGIC REGISTER=================================//
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailTxt.getText().toString();
                String userPassword = passwordTxt.getText().toString();
                String userCfPassword = repasswordTxt.getText().toString();
                // if user input email,pass.....  null
                if(userEmail.equals("")|| userPassword.equals("")|| userCfPassword.equals("")){
                    Toast.makeText(RegisterActivity.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    //if pass < 8 length
                    if(!validPassword(userPassword)){
                        Toast.makeText(RegisterActivity.this, "Please enter greater than 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // if pass match cf pass
                        if(userPassword.equals(userCfPassword)){
                            Boolean checkEmail = db.CheckEmail(userEmail);
                            // if regex email valid
                            if(!validEmail(userEmail)){
                                Toast.makeText(RegisterActivity.this, "Please enter a valid filed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(!checkEmail){ // if email not existed in database
                                    Boolean insert = db.insertData(userEmail,userPassword);
                                    if(insert) {
                                        // show message success full
                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        // nav to home layout
                                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                        startActivity(intent);
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
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private boolean validPassword(String password){
        password = password.trim();
        return password.length() >= 8;
    }
}