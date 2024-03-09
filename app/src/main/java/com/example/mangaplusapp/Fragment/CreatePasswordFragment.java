package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePasswordFragment extends Fragment {

    EditText getUserNameTxt,getUserPasswordTxt,getUserRePasswordTxt;
    String emailUser;
    UserDBHelper dbHelper;
    AppCompatButton btnSubmit;
    RelativeLayout layoutInput;
    RelativeLayout layoutInputUserName;
    ImageButton backRegisterBtn;
    String userId;
    String hardIMG = "https://media.discordapp.net/attachments/1062386055258570804/1212303621354561556/logo_test.png?ex=65f158ba&is=65dee3ba&hm=0f1f490837c7544ada46aca5e652a769a8c0f02f7529133bc5c9f981e719b992&=&format=webp&quality=lossless&width=525&height=525";
    Uri imgDefault = Uri.parse(hardIMG);

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
        emailUser =preferences.getString("user_email",null);
        layoutInputUserName=root.findViewById(R.id.InputUNCP);
        btnSubmit = root.findViewById(R.id.btnSubmitInfo);
        getUserNameTxt = root.findViewById(R.id.userNewNameTxt);
        getUserPasswordTxt = root.findViewById(R.id.userNewPasswordTxt);
        getUserRePasswordTxt = root.findViewById(R.id.userRePasswordTxt);
        layoutInput=root.findViewById(R.id.userInputInfo_layout);
        backRegisterBtn=root.findViewById(R.id.backRegisterBtn);
        backRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new VerificationFragment(),false);
            }
        });

        dbHelper.checkEmailExists(emailUser, new UserDBHelper.userCheckFirebaseListener() {
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String newPassword = getUserPasswordTxt.getText().toString().trim();
            // Cập nhật mật khẩu trên Firebase Authentication
            user.updatePassword(newPassword)
                    .addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            // Cập nhật mật khẩu thành công trên Authentication
                            Toast.makeText(getContext(), "Password update successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
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
        mAuth.createUserWithEmailAndPassword(emailUser, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName) // Đặt tên người dùng
                                .setPhotoUri(imgDefault)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                    } else {
                                        Log.w("", "Failed to update user profile.", task1.getException());
                                    }
                                });
                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                        usersRef.child(userId).setValue(new User(userId,emailUser));
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