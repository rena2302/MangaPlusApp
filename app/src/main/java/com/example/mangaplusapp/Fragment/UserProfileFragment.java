package com.example.mangaplusapp.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.example.mangaplusapp.Activity.Base.EditControlActivity;
import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserProfileFragment extends Fragment {
    ImageView TabDialog,getUserAvtIMG;
    UserDBHelper db;
    CardView avtContainer;
    TextView getUserNameInfoTxt,getUserNameTittleTxt,getUserEmailTxt,getUserPasswordTxt,HeaderEmail;
    String userId;
    String userEmail,userName,userAvt;
    LinearLayout navToRegion;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static final int SELECT_IMAGE = 100;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId=currentUser.getProviderId();
        Log.d("Profile", "ID " + userId);

        if(!userId.isEmpty()){
            userName = currentUser.getDisplayName();
            userEmail = currentUser.getEmail();
            userAvt = String.valueOf(currentUser.getPhotoUrl());
            userExists();
            userSetIMG();
        }
        else{
            handleNotLoggedIn();
        }
        //****************************************************************************************//
        return root;
    }
    private void startAct(){
        getActivity().finish(); // Kết thúc hoạt động hiện tại
        Intent intent = getActivity().getIntent(); // Lấy intent hiện tại
        getActivity().startActivity(intent); // Khởi động lại hoạt động
    }
    private boolean isLoggedIn(){
        return currentUser != null;
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
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();
            currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Glide.with(this).load(uri).into(getUserAvtIMG);
                    Toast.makeText(getActivity(), "Update Avatar Successful", Toast.LENGTH_SHORT).show();
                    startAct();
                }
                else{
                    Toast.makeText(getActivity(), "Update Avatar Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void openActivityWithFragmentData(String data) {
        Intent intent = new Intent(getContext(), EditControlActivity.class);
        intent.putExtra("FRAGMENT_DATA", data);
        startActivity(intent);
    }
    private void showDialog()
    {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_profile);

        LinearLayout editprofile=dialog.findViewById(R.id.EditProfile);

        editprofile.setOnClickListener(v->{
            dialog.dismiss();
//            loadFragment(new EditNameFragment(),false);
            getActivity().finish();
            openActivityWithFragmentData("Fragment1Data");
        });
        LinearLayout editemail=dialog.findViewById(R.id.ChangedEmailBtn);

        editemail.setOnClickListener(v->{
            dialog.dismiss();
//            loadFragment(new ChangeEmailFragment(),false);
            getActivity().finish();
            openActivityWithFragmentData("Fragment2Data");
        });

        LinearLayout editpass=dialog.findViewById(R.id.EditPass);

        editpass.setOnClickListener(v->{
            dialog.dismiss();
//            loadFragment(new ChangePasswordFragment(),false);
            getActivity().finish();
            openActivityWithFragmentData("Fragment3Data");
        });

        navToRegion=dialog.findViewById(R.id.navToLanguage);
        navToRegion.setOnClickListener(v->{
            dialog.dismiss();
//            loadFragment(new RegionFragment(),false);
            getActivity().finish();
            openActivityWithFragmentData("Fragment4Data");
        });

        AppCompatButton logout=dialog.findViewById(R.id.Logout);

        if(isLoggedIn()){
            logout.setHint("Log Out");
            ///////===========================Begin Logout=========================/////////////////////
            logout.setOnClickListener(v -> {
                // Clear session and navigate to login activity
                try {
                    FirebaseAuth.getInstance().signOut();
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
                getActivity().finish();
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

}