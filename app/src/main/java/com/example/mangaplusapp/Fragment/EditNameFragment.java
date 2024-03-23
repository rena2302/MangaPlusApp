package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Activity.User.MainActivity;
import com.example.mangaplusapp.Helper.ActionHelper.KeyBoardHelper;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

public class EditNameFragment extends Fragment {
    ImageButton Backbtn;
    EditText getUserName;
    String userName;
    UserDBHelper dbhelper;
    AppCompatButton submit;
    String userId;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public EditNameFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_edit_name, container, false);
        ScrollView mainLayout=root.findViewById(R.id.OverEditName);
        KeyBoardHelper.ActionRemoveKeyBoardForFragment(mainLayout,requireContext());
        Backbtn=root.findViewById(R.id.backEditProfileBtn);
        //===================================Data=================================================//
        dbhelper = new UserDBHelper(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userId= currentUser.getProviderId();
        //****************************************************************************************//
        getUserName = root.findViewById(R.id.userNewNameTxt);
        submit= root.findViewById(R.id.btnSubmitInfoName);

        if(!userId.isEmpty()){
               navigate();
        } else{
            return root;
        }
        return  root;
    }

    private boolean setName(){
        userName = getUserName.getText().toString();
        if(!dbhelper.validName(userName)){
            return false;
        }else{
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build();
            currentUser.updateProfile(profileUpdates).addOnCompleteListener(requireActivity(), task ->{
                if(task.isSuccessful()){
                    Intent intent=new Intent(getContext(), MainActivity.class);
                    intent.putExtra("BackToProfile", 1);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    // Xử lý lỗi khi cập nhật thông tin người dùng thất bại
                    Exception e = task.getException();
                    if (e != null) {
                        // Log hoặc hiển thị thông báo lỗi
                        e.printStackTrace();
                    }
                }
            });
            currentUser.updateProfile(profileUpdates);
            return true;
        }
    }

    private void navigate(){
        Backbtn.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), MainActivity.class);
            intent.putExtra("BackToProfile", 1);
            startActivity(intent);
            getActivity().finish();
        });
        submit.setOnClickListener(v->{
            if(setName()){
                Toast.makeText(getActivity(),R.string.update_username_successful, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),R.string.username_min_length,Toast.LENGTH_SHORT).show();
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
