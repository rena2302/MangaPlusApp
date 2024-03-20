package com.example.mangaplusapp.Fragment;

import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.Helper.LoadHelper.LoadFragment;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    ImageButton backBtn;
    EditText getOldPass;
    String oldPass;
    UserDBHelper dbHelper;
    AppCompatButton submit;
    String userID;
    LoadFragment fragmentHelper;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    AuthCredential credential;
    String data;
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
        submit= root.findViewById(R.id.btnSubmitInfo_Edit);
        //****************************************************************************************//
        //========================================GET DATA========================================//
        dbHelper = new UserDBHelper(requireContext());
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID=currentUser.getProviderId();
        navigate();
        //****************************************************************************************//
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
            CheckOldEmail();
        });
    }
    private void CheckOldEmail()
    {
        Log.d("Email", currentUser.getEmail());

        oldPass=getOldPass.getText().toString();
        Log.d("Password", oldPass);
        credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPass);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fragmentHelper = new LoadFragment();
                        fragmentHelper.loadFragment(getParentFragmentManager(), new SuccessFragment(), false, R.id.editFmContainer);
                    } else {
                        Toast.makeText(getContext(),"Invalid password",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
