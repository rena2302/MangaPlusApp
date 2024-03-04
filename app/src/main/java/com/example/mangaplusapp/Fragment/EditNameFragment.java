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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;

public class EditNameFragment extends Fragment {
    ImageButton Backbtn;
    EditText getUserName;
    String userName;
    UserDBHelper dbhelper;
    AppCompatButton submit;
    int userId;
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
        Backbtn=root.findViewById(R.id.backEditProfileBtn);
        //===================================Data=================================================//
        dbhelper = new UserDBHelper(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId=preferences.getInt("user_id",-1);
        //****************************************************************************************//
        getUserName = root.findViewById(R.id.userNewNameTxt);
        submit= root.findViewById(R.id.btnSubmitInfoName);

        if(userId!=-1){
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
            dbhelper.UpdateUserName(userId,userName);
            return true;
        }
    }
    private void navigate(){
        Backbtn.setOnClickListener(v->{
            loadFragment(new UserProfileFragment(),false);
        });
        submit.setOnClickListener(v->{
            if(setName()){
                Toast.makeText(getActivity(), "Update User Name Successful", Toast.LENGTH_SHORT).show();
//                loadFragment(new UserProfileFragment(),false);
                startAct();
            }
            else{
                Toast.makeText(getActivity(),"Please enter user name >= 5 ",Toast.LENGTH_SHORT).show();
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
    private void startAct(){
        getActivity().finish(); // Kết thúc hoạt động hiện tại
        Intent intent = getActivity().getIntent(); // Lấy intent hiện tại
        getActivity().startActivity(intent); // Khởi động lại hoạt động
    }
}
