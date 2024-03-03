package com.example.mangaplusapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.mangaplusapp.R;

import java.util.Locale;


public class RegionFragment extends Fragment {

    Spinner spinner;
    Context context;
    String[] languages = {"Select language","English","VietNam","Hindi","Korean"};
    public RegionFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_region, container, false);
        spinner =root.findViewById(R.id.regionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                if(selectedLanguage.equals("English")){
                    setLocal(getActivity(),"en");
                    startAct();
                }else if (selectedLanguage.equals("Hindi")){
                    setLocal(getActivity(),"inc");
                   startAct();
                }
                else if (selectedLanguage.equals("Korean")){
                    setLocal(getActivity(),"ko");
                    startAct();
                }
                else if(selectedLanguage.equals("VietNam")){
                    setLocal(getActivity(),"vi");
                    startAct();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }
    private void startAct(){
        getActivity().finish(); // Kết thúc hoạt động hiện tại
        Intent intent = getActivity().getIntent(); // Lấy intent hiện tại
        getActivity().startActivity(intent); // Khởi động lại hoạt động
    }
    private void setLocal(Activity activity, String langCode){
        Locale locale = new Locale(langCode);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}