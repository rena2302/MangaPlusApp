package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SuccessFragment extends Fragment {
    String userEmail;
    AppCompatButton btn ;
    public SuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_success, container, false);
        btn=root.findViewById(R.id.navToLogin);
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userEmail=preferences.getString("user_email","");
        if(!userEmail.isEmpty()){
            sendResetPasswordByEmail(userEmail);
            navigate();
        }
        else{
            Log.d("User email ", "null ne");
        }
        return root;
    }
    private void navigate(){
        btn.setOnClickListener(v->{
            Intent intent = new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
        });
    }
    private void sendResetPasswordByEmail (String userEmail){
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                            // Thông báo cho người dùng rằng email đã được gửi thành công
                        } else {
                            Toast.makeText(getContext(), "Failed to send password reset email: ", Toast.LENGTH_SHORT).show();
                            // Xử lý trường hợp gửi email thất bại
                        }
                    }
                });
    }
}