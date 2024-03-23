    package com.example.mangaplusapp.Activity.User;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ScrollView;
    import android.widget.Toast;

    import androidx.appcompat.widget.AppCompatButton;

    import com.example.mangaplusapp.Activity.Base.BaseActivity;
    import com.example.mangaplusapp.Activity.Base.LoginActivity;
    import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
    import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
    import com.example.mangaplusapp.R;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    public class RegisterActivity extends BaseActivity {
        EditText userInputEmail;
        String userEmail;
        AppCompatButton signUpBtn;
        AppCompatButton haveAccount;
        ImageButton btnToLogin;
        UserDBHelper db;
        DatabaseReference usersRef;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            //Ẩn KeyBoard
            ScrollView mainLayout = findViewById(R.id.ResEmailOverlay);
            KeyBoardHelper.ActionRemoveKeyBoardForActivity(mainLayout,this);
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
            usersRef = FirebaseDatabase.getInstance().getReference("users");
            //===================================DATABASE=============================================//
            //****************************************************************************************//
            //===================================BEGIN LOGIC REGISTER=================================//
            signUpBtn.setOnClickListener(v-> {
                userEmail = userInputEmail.getText().toString();
                checkEmailExistsAndProceed(userEmail);
                Log.d("register", "Checked");
            });
        }
        //======================================END LOGIC REGISTER====================================//
        private void checkEmailExistsAndProceed(final String email) {
            Log.d("Register", "checkEmailExistsAndProceed: checked");
            db.checkEmailExists(email, new UserDBHelper.userCheckFirebaseListener() {
                @Override
                public void onEmailCheckResult(boolean exists) {
                    if (exists) {
                        Log.d("register","Email exists" );
                        Log.d("register","Case to forgot" );
                        Toast.makeText(RegisterActivity.this, R.string.emailExists, Toast.LENGTH_SHORT).show();
                    } else {
                        if (db.validEmail(email)) {
                            Log.d("register","Case to Register" );
                            Intent intent = new Intent(RegisterActivity.this, ForgotControlActivity.class);
                            Log.d("Register", "email not exist in DB");
                            intent.putExtra("EMAIL", email);
                            startActivity(intent);

                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.typeEmailValid, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
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