package com.example.mangaplusapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    ImageButton backBtn;
    EditText getOldPass,getNewPass,getCfPass;
    String oldPass,newPass,cfPass,authPassword;
    UserDBHelper dbHelper;
    AppCompatButton submit;
    String userID;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public  ChangePasswordFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_change_pasword, container, false);
        ScrollView mainLayout=root.findViewById(R.id.OverlayChangePass);
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(mainLayout,requireContext());
        //========================================GET ID==========================================//
        backBtn=root.findViewById(R.id.backChangedPassBtn);
        getOldPass = root.findViewById(R.id.EditOldPassTxt);
        getNewPass = root.findViewById(R.id.NewPasswordTxt);
        getCfPass = root.findViewById(R.id.userRePasswordTxt);
        submit= root.findViewById(R.id.btnSubmitInfo_Edit);
        //****************************************************************************************//
        //========================================GET DATA========================================//
        dbHelper = new UserDBHelper(requireContext());
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID=currentUser.getProviderId();

        //****************************************************************************************//
        if(!userID.isEmpty()){
            navigate();
        }
        return  root;
    }
    private void navigate(){
        backBtn.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), MainActivity.class);
            intent.putExtra("BackToProfile", 1);
            startActivity(intent);
            getActivity().finish();
        });
        submit.setOnClickListener(v->{
            sendResetPasswordByEmail(currentUser.getEmail());
        });

    }
    private void sendResetPasswordByEmail (String userEmail){
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getContext(), MainActivity.class);
                            intent.putExtra("BackToProfile", 1);
                            startActivity(intent);
                            getActivity().finish();
                            // Thông báo cho người dùng rằng email đã được gửi thành công
                        } else {
                            Toast.makeText(getContext(), "Failed to send password reset email: ", Toast.LENGTH_SHORT).show();
                            // Xử lý trường hợp gửi email thất bại
                        }
                    }
                });
    }

}
