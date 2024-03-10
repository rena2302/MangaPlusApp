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

import androidx.appcompat.widget.AppCompatButton;

import com.example.mangaplusapp.Activity.User.ForgotControlActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Activity.User.RegisterActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.ModelAndPresenter.Login.LoginPresenter;
import com.example.mangaplusapp.ModelAndPresenter.Login.MVPLoginView;
import com.example.mangaplusapp.R;

public class LoginActivity extends BaseActivity implements MVPLoginView {
    ScrollView scrollView;
    EditText emailTxt, passwordTxt;
    TextView forgotPasswordTxt,toSignUpTxt;
    AppCompatButton btnLoginTxt;
    UserDBHelper db;
    //Create sign in Google

    ImageView googleBtn;
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
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
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