package com.example.mangaplusapp.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;

public class UserProfileFragment extends Fragment {
    ImageView TabDialog,getUserAvtIMG;
    UserDBHelper db;
    CardView avtContainer;
    TextView getUserNameInfoTxt,getUserNameTittleTxt,getUserEmailTxt,getUserPasswordTxt,HeaderEmail;
    int userId;
    String userEmail,userPassword,userName,userAvt;
    LinearLayout navToRegion;
    private static final int SELECT_IMAGE = 100;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container,false);
        //=========================================Get id=========================================//
        getUserNameInfoTxt = root.findViewById(R.id.userName_info);
        getUserNameTittleTxt = root.findViewById(R.id.userName_Tittle);
        getUserEmailTxt = root.findViewById(R.id.userEmail_info);
        getUserPasswordTxt = root.findViewById(R.id.userPassword_info);
        HeaderEmail=root.findViewById(R.id.userName_Email);
        TabDialog=root.findViewById(R.id.Tab_showdialog);
        getUserAvtIMG = root.findViewById(R.id.Avatar_Profile_Image);
        avtContainer = root.findViewById(R.id.Avatar_Profile_Card);
        TabDialog.setOnClickListener(v->{
            showDialog();
        });
        //****************************************************************************************//
        //=========================================Get data=======================================//
        db = new UserDBHelper(requireContext());
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userId= preferences.getInt("user_id",-1);
        userName = db.getUserName(userId);
        userPassword = db.getUserPassword(userId);
        userEmail = db.getUserEmail(userId);
        userAvt = db.getPicture(userId);
        if(userId!=-1){
            userExists();
            userSetIMG();
        }
        else{
            handleNotLoggedIn();
        }
        //****************************************************************************************//
        return root;
    }
    private boolean isLoggedIn(){
        return db.isUserLoggedIn();
    }
    private void handleNotLoggedIn() {
        getUserNameInfoTxt.setText("Guest");
        getUserNameTittleTxt.setText("Guest");
        HeaderEmail.setText("Not logged in");
        getUserEmailTxt.setText("Not logged in");
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
    void userExists(){
        getUserEmailTxt.setText(userEmail);
        getUserPasswordTxt.setText("..........");
        getUserNameInfoTxt.setText(userName);
        getUserNameTittleTxt.setText(userName);
        HeaderEmail.setText(userEmail);
        Glide.with(this).load(userAvt).into(getUserAvtIMG);
    }
    void userSetIMG(){
        avtContainer.setOnClickListener(v->{
            Intent intent = new Intent (Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent,"Select Image"),
                    SELECT_IMAGE
            );
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            Uri uri = data.getData();
            // Hiển thị hình ảnh được chọn
            // Thêm dữ liệu vào cơ sở dữ liệu
            Log.d("ADD URI ", "TRUE" + uri.toString());
            db.updatePicture(userId,uri);
            String spoilPicture = db.getPicture(userId);
            Glide.with(this).load(spoilPicture).into(getUserAvtIMG);

            Toast.makeText(getActivity(), "Update Avatar Successful", Toast.LENGTH_SHORT).show();

        }
    }
    private void showDialog()
    {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_profile);

        LinearLayout editprofile=dialog.findViewById(R.id.EditProfile);

        editprofile.setOnClickListener(v->{
            dialog.dismiss();
            loadFragment(new EditNameFragment(),false);
        });
        LinearLayout editpass=dialog.findViewById(R.id.EditPass);

        editpass.setOnClickListener(v->{
            dialog.dismiss();
            loadFragment(new ChangePasswordFragment(),false);
        });

        navToRegion=dialog.findViewById(R.id.navToLanguage);
        navToRegion.setOnClickListener(v->{
            dialog.dismiss();
            loadFragment(new RegionFragment(),false);
        });

        AppCompatButton logout=dialog.findViewById(R.id.Logout);

        if(isLoggedIn()){
            logout.setHint("Log Out");
            ///////===========================Begin Logout=========================/////////////////////
            logout.setOnClickListener(v -> {
                // Clear session and navigate to login activity
                try {
                    clearSession();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Log out successful", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Something was wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            logout.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            });
            ////////===========================End Logout=========================//////////////////////
        }

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void clearSession() {
        db.clearUserSession();
    }

}