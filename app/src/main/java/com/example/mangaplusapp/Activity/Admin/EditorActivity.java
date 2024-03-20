package com.example.mangaplusapp.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Fragment.EditorFragment;
import com.example.mangaplusapp.R;


public class EditorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        loadFragmentBasic(new EditorFragment(),false, getIntent().getExtras());
        ImageButton backBtn = (ImageButton) findViewById(R.id.editBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadFragmentBasic(Fragment fragment, boolean isAppInitialized, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragment.setArguments(bundle);
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.editFmContainer, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.editFmContainer, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}