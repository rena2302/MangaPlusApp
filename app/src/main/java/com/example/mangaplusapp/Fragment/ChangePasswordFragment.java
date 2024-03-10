package com.example.mangaplusapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
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
            loadFragment(new UserProfileFragment(),false);
        });
        submit.setOnClickListener(v->{
            userNewPass();
        });

    }
    private void userNewPass(){
        oldPass= getOldPass.getText().toString();
        newPass = getNewPass.getText().toString();
        cfPass= getCfPass.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPass);
        if(oldPass.isEmpty()||newPass.isEmpty()||cfPass.isEmpty()){
            Toast.makeText(getContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();
        }
        else
        {
            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if(newPass.equals(cfPass)){
                                if(dbHelper.validPassword(newPass)){
                                    //=================================Success case===============================//
                                    UpdatePass(newPass);
                                }
                                else{
                                    Toast.makeText(getContext(),"Please enter password length >= 8 or <= 12 ",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getContext(),"Password and Confirm password not match",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(),"Password not match",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void UpdatePass(String newPass){
        currentUser.updatePassword(newPass).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Toast.makeText(getContext(),"Update password successful",Toast.LENGTH_SHORT).show();
                loadFragment(new UserProfileFragment(),false);
                //*****************************************************************************//
            }
            else{
                Toast.makeText(getContext(),"Update password Failed by system",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
}
