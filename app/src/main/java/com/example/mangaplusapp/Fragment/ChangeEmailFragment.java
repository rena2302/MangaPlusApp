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

public class ChangeEmailFragment extends Fragment {
    AppCompatButton SendOtpBtn;
    UserDBHelper db;
    EditText InputEmail;
    ImageButton Backbtn;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String NewEmail;
    public ChangeEmailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CONTAINER", "onCreateView: Check");
        View root = inflater.inflate(R.layout.fragment_changeemail, container, false);
        ScrollView mainLayout = root.findViewById(R.id.OverlayEditEmail);
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(mainLayout, requireContext());
        SendOtpBtn = root.findViewById(R.id.btnSubmitInfoName);
        InputEmail = root.findViewById(R.id.userNewNameTxt);
        Backbtn = root.findViewById(R.id.backEditEmailBtn);
        db = new UserDBHelper(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        VeritificationAccount();
        return root;
    }
    private void VeritificationAccount()
    {
        AuthCredential credential = EmailAuthProvider
                .getCredential("hoangphongcuade@gmail.com", "26112004");

        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(),"CheckSuccessfull",Toast.LENGTH_SHORT).show();
                            SendOtpBtn.setOnClickListener(v->{
                                NewEmail=InputEmail.getText().toString();
                                currentUser.updateEmail(NewEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(),"UpdateSuccesfull",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getContext(),"Update Failed",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            });
                        }
                        else {
                            Toast.makeText(getContext(),"Check Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
