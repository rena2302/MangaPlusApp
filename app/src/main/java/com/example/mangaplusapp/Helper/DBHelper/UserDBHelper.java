package com.example.mangaplusapp.Helper.DBHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class UserDBHelper  {
    public UserDBHelper(Context context) {
        super();
    }
    //==========================================VALIDATION========================================//
    @SuppressLint("Range")
    public boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public boolean validPassword(String password){
        password = password.trim();
        return password.length() >= 8;
    }
    public String hashPassword(String pass){
        return BCrypt.hashpw(pass,BCrypt.gensalt());
    }
    public boolean CheckHashPassword(String password,String hashPassword){
        return BCrypt.checkpw(password,hashPassword);
    }
    public boolean validName(String userName){
        if(!(userName.trim().length()>=5 && userName.trim().length()<=12))
            return false;
        return true;
    }
    public interface userCheckFirebaseListener {
        void onEmailCheckResult(boolean exists);
    }

    public void checkEmailExists(String email, final userCheckFirebaseListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = dataSnapshot.exists();
                listener.onEmailCheckResult(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onEmailCheckResult(false); // Trả về false nếu xảy ra lỗi
            }
        });
    }
}
