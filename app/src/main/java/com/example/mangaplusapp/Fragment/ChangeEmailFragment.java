package com.example.mangaplusapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Activity.User.ForgotControlActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Activity.User.RegisterActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangeEmailFragment extends Fragment {
    AppCompatButton AcceptnewEmail, SubmitChange;
    UserDBHelper db;
    EditText InputEmail;
    ImageButton Backbtn;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String NewEmail,OldEmail,OldPass;
    EditText oldEmail, oldPass;

    public ChangeEmailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_changeemail, container, false);
        ScrollView mainLayout = root.findViewById(R.id.OverlayEditEmail);
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(mainLayout, requireContext());
        AcceptnewEmail = root.findViewById(R.id.btnchangeNewEmail);
        InputEmail = root.findViewById(R.id.userNewNameTxt);
        Backbtn = root.findViewById(R.id.backEditEmailBtn);
        db = new UserDBHelper(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Backbtn.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), MainActivity.class);
            intent.putExtra("BackToProfile", 1);
            startActivity(intent);
            getActivity().finish();
        });
        AcceptnewEmail.setOnClickListener(v->{
            NewEmail=InputEmail.getText().toString();
                checkEmailExistsAndProceed(NewEmail);
        });
        return root;
    }
    private void checkEmailExistsAndProceed(final String email) {
        Log.d("Register", "checkEmailExistsAndProceed: checked");
        db.checkEmailExists(email, new UserDBHelper.userCheckFirebaseListener() {
            @Override
            public void onEmailCheckResult(boolean exists) {
                if (exists) {
                    Toast.makeText(getContext(), "This email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.validEmail(email)) {
                        ShowDialog();

                    } else {
                        Toast.makeText(getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void changeEmailWithoutUpdateEmail(String OldEmail, String OldPass) {
        if (currentUser != null) {

                AuthCredential credential = EmailAuthProvider.getCredential(OldEmail, OldPass);
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> reauthTask) {
                                if (reauthTask.isSuccessful()) {
                                    NewEmail=InputEmail.getText().toString();
                                    changeNewEmail(NewEmail);
                                } else {
                                    Toast.makeText(getContext(), "Authentication failure !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } 
            else {
                Toast.makeText(getContext(),"Invalid email",Toast.LENGTH_SHORT).show();
            }
        }
        
    private void changeNewEmail(String newEmail)
    {
        currentUser.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getContext(), MainActivity.class);
                            intent.putExtra("BackToProfile", 1);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void ShowDialog()
    {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_check_oldemailpass);

        oldEmail=dialog.findViewById(R.id.EmailTxt);
        oldPass=dialog.findViewById(R.id.OldPassTxt);
        SubmitChange=dialog.findViewById(R.id.btnSubmitChange);

        SubmitChange.setOnClickListener(v->{
            OldEmail=oldEmail.getText().toString();
            OldPass=oldPass.getText().toString();
            if(OldPass.isEmpty()||OldPass.isEmpty())
            {
                Toast.makeText(getContext(),"Your email or password invalid!",Toast.LENGTH_SHORT).show();
            }
            else {
                changeEmailWithoutUpdateEmail(OldEmail,OldPass);
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

    }
}
