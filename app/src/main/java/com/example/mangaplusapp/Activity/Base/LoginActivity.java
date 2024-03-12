package com.example.mangaplusapp.Activity.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mangaplusapp.Activity.User.ForgotControlActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Activity.User.RegisterActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.ModelAndPresenter.Login.LoginPresenter;
import com.example.mangaplusapp.ModelAndPresenter.Login.MVPLoginView;
import com.example.mangaplusapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements MVPLoginView {
    ScrollView scrollView;
    EditText emailTxt, passwordTxt;
    TextView forgotPasswordTxt,toSignUpTxt;
    AppCompatButton btnLoginTxt;
    UserDBHelper db;
    //Create sign in Google
    FirebaseAuth mAuth;
    FirebaseDatabase databaseFirebase;
    GoogleSignInClient mGoogleSignInClient;
    ImageView googleBtn;
    int RC_SIGN_IN = 20;
    //End sign in Google
    private LoginPresenter loginPresenter;
    //Call PresenterLogin
    private LoginPresenter ResAction;
    private LoginPresenter ForAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //===============================Begin get id for login basic=============================//
        scrollView=findViewById(R.id.OverlayLogin);
        toSignUpTxt = findViewById(R.id.toSignUp);
        emailTxt = (EditText) findViewById(R.id.editTextEmail);
        passwordTxt = (EditText) findViewById(R.id.editTextPassword);
        btnLoginTxt = (AppCompatButton) findViewById(R.id.loginBtn_act);
        forgotPasswordTxt = (TextView) findViewById(R.id.forgotPassword_act);
        //================================End get id for login basic==============================//
        //****************************************************************************************//
        //================================Begin get data for login basic==========================//
        KeyBoardHelper.ActionRemoveKeyBoardForActivity(scrollView,this);
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //================================END get data for login basic============================//
        //****************************************************************************************//
        //===============================Begin get id for login with social=======================//
        googleBtn = findViewById(R.id.googleBtn_login);
        mAuth = FirebaseAuth.getInstance();
        databaseFirebase = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        googleBtn.setOnClickListener(v->{
            googleSignIn();
        });
        //===============================End get id for login with social=========================//
        //****************************************************************************************//
        //===============================CONNECT DATABASE=========================================//
        db = new UserDBHelper(this);
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
        //===============================CREATE CALL PRESENTER====================================//
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        loginPresenter=new LoginPresenter(this);
        //===============================END CALL PRESENTER====================================//
        final Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_btn);
        btnLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            // this event final
            public void onClick(View v) {
                btnLoginTxt.startAnimation(scaleAnimation);
                loginPresenter.receivedHandleLogin(emailTxt,passwordTxt,db,editor);
                //đang ở ActView thông báo cho presenter khi đươc click sự kiện
            }
        });
        //===============================END LOGIC LOGIN BASIC====================================//
        //****************************************************************************************//
        //****************************************************************************************//
        //===============================Begin login with social==================================//
    }

    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                // Lấy mã lỗi từ ApiException
                int errorCode = e.getStatusCode();
                Toast.makeText(this, "Google sign in failed. Error code: " + errorCode, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Nếu không phải ApiException, hiển thị lỗi khác
                Toast.makeText(this, "An unexpected error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("idUser",user.getUid());
//                            map.put("name",user.getDisplayName());
                            map.put("userEmail",user.getEmail());
//                            map.put("profile",user.getPhotoUrl().toString());
                            databaseFirebase.getReference().child("Users").child(user.getUid()).setValue(map);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Something was wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void navigateLayout(){
        //==================================BEGIN NAV TO SIGN UP==================================//
        toSignUpTxt.setOnClickListener(v -> {
            ResAction =new LoginPresenter(this);
            ResAction.RegisterAction();
        });
        //====================================END NAV TO SIGN UP==================================//
        //==============================BEGIN NAV TO FORGOT PASSWORD==============================//
        forgotPasswordTxt.setOnClickListener(v->{
            ForAction=new LoginPresenter(this);
            ForAction.ForAction();
        });
        //****************************************************************************************//
    }
    //Callback by Presenter
    @Override
    public void LoginSuccess() {
        Toast.makeText(LoginActivity.this,"Sign Ip Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void LoginFailed() {
        Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void LoginDefalt() {
        Toast.makeText(LoginActivity.this,"Please enter all fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ResActionPage() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // bat su kien
        startActivity(intent); // chay su kien
    }

    @Override
    public void ForgotAction() {
        Intent intent = new Intent(LoginActivity.this, ForgotControlActivity.class); // bat su kien
        startActivity(intent); // chay su kien
    }

    @Override
    public void Emailnoitvalid() {
        Toast.makeText(LoginActivity.this,"        Email not valid \nEx:YourEmail@gmail.com", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Passwordnotvalid() {
        Toast.makeText(LoginActivity.this,"          Password not valid \nMust be more than 8 characters", Toast.LENGTH_SHORT).show();
    }
}