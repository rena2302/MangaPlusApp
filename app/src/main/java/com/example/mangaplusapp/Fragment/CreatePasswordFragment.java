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
    String userId;

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
        userId=preferences.getString("user_id","");
        if(userId.isEmpty())
        {
            Log.d("sss", "null-1 ");
        }
        else
        {
            Log.d("aaa", userId);
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

        dbHelper.checkEmailExists(userEmail, new UserDBHelper.userCheckFirebaseListener() {
            @Override
            public void onEmailCheckResult(boolean exists) {
                if(exists){
                    //=============================ForgotPassword Case============================//
                    layoutInput.removeView(layoutInputUserName);
                    btnSubmit.setOnClickListener(v->{
                        updatePassword();
                    });
                }
                else{
                    //=============================NewAccount Case================================//
                    btnSubmit.setOnClickListener(v->{
                        registerNewAccount();
                    });
                }
            }
        });
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String newPassword = getUserPasswordTxt.getText().toString().trim();
            // Cập nhật mật khẩu trên Firebase Authentication
            user.updatePassword(newPassword)
                    .addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            // Cập nhật mật khẩu thành công trên Authentication

                            // Tiếp tục cập nhật mật khẩu trên Realtime Database
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            usersRef.child(userId).child("password").setValue(dbHelper.hashPassword(newPassword))
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            // Cập nhật mật khẩu thành công trên Realtime Database
                                            Toast.makeText(getContext(), "Password update successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getContext(), LoginActivity.class));
                                        } else {
                                            // Cập nhật mật khẩu thất bại trên Realtime Database
                                            Toast.makeText(getContext(), "Realtime Database update failed: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Cập nhật mật khẩu thất bại trên Authentication
                            Toast.makeText(getContext(), "Authentication update failed: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

        // Thực hiện đăng ký mới bằng Firebase Auth -> convert to Realtime Database  = cách sử dụng nút User
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userId).setValue(new User(userName, userEmail,dbHelper.hashPassword(userPassword)));

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