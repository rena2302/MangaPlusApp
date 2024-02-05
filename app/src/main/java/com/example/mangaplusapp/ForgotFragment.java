package com.example.mangaplusapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ForgotFragment extends Fragment {
    Button SendOtpBtn;
    public ForgotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_forgot, container, false);
        SendOtpBtn = root.findViewById(R.id.btnForgotSendOtp);
        return root;
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            // sau nay sua lai theo vi tri va rut gon
        }
        fragmentTransaction.commit();
    }
    void navigateLayout(){
        //==================================BEGIN NAV TO VERIFICATION==================================//
        SendOtpBtn.setOnClickListener(e->{
            loadFragment(new VerificationFragment(),false);
        });
        //==================================END NAV TO VERIFICATION==================================//

    }
}