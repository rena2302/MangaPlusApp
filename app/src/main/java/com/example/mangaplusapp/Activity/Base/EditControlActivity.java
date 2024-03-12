package com.example.mangaplusapp.Activity.Base;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mangaplusapp.Fragment.ChangeEmailFragment;
import com.example.mangaplusapp.Fragment.ChangePasswordFragment;
import com.example.mangaplusapp.Fragment.EditNameFragment;
import com.example.mangaplusapp.Fragment.RegionFragment;
import com.example.mangaplusapp.Fragment.SuccessFragment;
import com.example.mangaplusapp.Helper.LoadHelper.LoadFragment;
import com.example.mangaplusapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditControlActivity extends BaseActivity{
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_edit);
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, EditControlActivity.class);
        intent.putExtra("KEYBack", "back");
        String fragmentData = getIntent().getStringExtra("FRAGMENT_DATA");
        switch (fragmentData) {
            case "Fragment1Data":
                loadFragment(new EditNameFragment(),false);
                break;
            case "Fragment2Data":
                loadFragment(new ChangeEmailFragment(),false);
                break;
            case "Fragment3Data":
                loadFragment(new SuccessFragment(),false);
                break;
            case "Fragment4Data":
                loadFragment(new RegionFragment(),false);
                break;
            default:
                // Mặc định hoặc xử lý nếu không có dữ liệu hợp lệ
                break;
        }
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.editFmContainer, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.editFmContainer, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
}
