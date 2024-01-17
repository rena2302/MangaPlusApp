package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import Database.CreateDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    TextView forgotPassword;
    Button btnLogin;
    CreateDatabase db;

    //Create sign in Google
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    //End sign in Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lay id
        setContentView(R.layout.activity_login);
        // Begin get id for login basic
        TextView toSignUp = findViewById(R.id.toSignUp);

        // End get id for login basic
        //////////////////////////////////
        //////////////////////////////////
        //Begin get id for login with social
        googleBtn = findViewById(R.id.googleBtn_login);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso); // activity va options
        //End get id for login with social
        //lay id
        toSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // bat su kien
            startActivity(intent); // chay su kien
        });
        // Begin Connect Login With Social

        // End Connect Login With Social

        //database
        // CONNECT DATABASE
        db = new CreateDatabase(this);
        db.open();
        // End Connect DataBase
        //BEGIN LOGIC LOGIN Basic

        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.loginBtn_act);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword_act);

        //su kien login
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
        // su kien forgot password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();

                String oldPassword = password.getText().toString();
                String newPassword = password.getText().toString();
                String confirmNewPassword=password.getText().toString();

                Boolean checkEmail = db.CheckEmail(userEmail);
                Boolean checkPassword = db.CheckPassword(oldPassword);

                if(oldPassword.equals("")||newPassword.equals("")||confirmNewPassword.equals("")||userEmail.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(checkEmail){
                        if(checkPassword){
                            if(newPassword.equals(confirmNewPassword)){
                                boolean resetPass = db.resetPassword(newPassword);
                                if(resetPass){
                                    Toast.makeText(LoginActivity.this,"Password updated", Toast.LENGTH_SHORT).show();
                                    // nav to login
                                }
                               else{
                                    Toast.makeText(LoginActivity.this,"Error some thing in database", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"New Password and Confirm Password not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Password not match in database", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Can not find your email account, try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // END forgot password

        //END LOGIC LOGIN Basic

        //Begin login with social

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
        //End login with social
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