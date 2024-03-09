package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Database.User;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePasswordFragment extends Fragment {

    EditText getUserNameTxt,getUserPasswordTxt,getUserRePasswordTxt;
    String userName_register,userEmail,userPassword,userRePassword;
    UserDBHelper dbHelper;
    AppCompatButton btnSubmit;
    RelativeLayout layoutInput;
    RelativeLayout layoutInputUserName;
    ImageButton backRegisterBtn;
    int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Ẩn keyboard
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_create_password, container, false);
        dbHelper= new UserDBHelper(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userId=preferences.getInt("user_id",-1);
        if(userId==-1)
        {
            Log.d("sss", "null-1 ");
        }
        else
        {
            Log.d("aaa", String.valueOf(userId));
        }
        layoutInputUserName=root.findViewById(R.id.InputUNCP);
        btnSubmit = root.findViewById(R.id.btnSubmitInfo);
        getUserNameTxt = root.findViewById(R.id.userNewNameTxt);
        getUserPasswordTxt = root.findViewById(R.id.userNewPasswordTxt);
        getUserRePasswordTxt = root.findViewById(R.id.userRePasswordTxt);
        layoutInput=root.findViewById(R.id.userInputInfo_layout);
        backRegisterBtn=root.findViewById(R.id.backRegisterBtn);
        userEmail =preferences.getString("user_email",null);

        backRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new VerificationFragment(),false);
            }
        });

        //===================================ForgotPassword Case==================================//
        if(dbHelper.CheckEmailExists(userEmail)){
            layoutInput.removeView(layoutInputUserName);
            btnSubmit.setOnClickListener(v->{
                updatePassword();
            });
        }
        //===================================New Account Case=====================================//
        else{
            btnSubmit.setOnClickListener(v->{
                registerNewAccount();
            });
        }
        return root;

    }
    private void updatePassword() {
        String userPassword = getUserPasswordTxt.getText().toString().trim();
        String userRePassword = getUserRePasswordTxt.getText().toString().trim();

        if (!userPassword.equals(userRePassword)) {
            Toast.makeText(getContext(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 8) {
            Toast.makeText(getContext(), "Please enter a password of at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.validPassword(userPassword)) {
            Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện cập nhật mật khẩu bằng Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            usersRef.child(userId).child("password").setValue(userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Cập nhật mật khẩu thành công
                            Toast.makeText(getContext(), "Password update successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        } else {
                            // Cập nhật mật khẩu thất bại
                            Toast.makeText(getContext(), "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void registerNewAccount() {
        String userName = getUserNameTxt.getText().toString().trim();
        String userPassword = getUserPasswordTxt.getText().toString().trim();
        String userRePassword = getUserRePasswordTxt.getText().toString().trim();

        if (!userPassword.equals(userRePassword)) {
            Toast.makeText(getContext(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 8) {
            Toast.makeText(getContext(), "Please enter a password of at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.validPassword(userPassword)) {
            Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.validName(userName)) {
            Toast.makeText(getContext(), "Please enter a user name of at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện đăng ký mới bằng Firebase Realtime Database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userId).setValue(new User(userName, userEmail));

                        Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(getContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.forgotContainer, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
}