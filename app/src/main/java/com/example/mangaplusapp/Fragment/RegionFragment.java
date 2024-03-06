package com.example.mangaplusapp.Fragment;

import android.annotation.SuppressLint;
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
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.mangaplusapp.R;

import java.util.Locale;

public class RegionFragment extends Fragment {

    AutoCompleteTextView spinner;
    AppCompatButton reset_btn;
    ArrayAdapter<String> adapter;
    String[] languages = {"English","VietNam","Hindi","Korean"};
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
        adapter = new ArrayAdapter<String>(requireContext(),R.layout.list_item_laguage,languages);
//        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        reset_btn=root.findViewById(R.id.Language_Apply);
        final String[] GetKey = new String[1];
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                if(selectedLanguage.equals("English")){
                    GetKey[0] ="en";
                }else if (selectedLanguage.equals("Hindi")){
                    GetKey[0] ="inc";
                }
                else if (selectedLanguage.equals("Korean")){
                    GetKey[0] ="ko";
                }
                else if(selectedLanguage.equals("VietNam")){
                    GetKey[0] ="vi";
                }
            }
        });
        reset_btn.setOnClickListener(v->{
            setLocal(requireContext(),GetKey[0]);
            startAct();
        });
        return root;
    }
    private void startAct(){
        getActivity().finish(); // Kết thúc hoạt động hiện tại
        Intent intent = getActivity().getIntent(); // Lấy intent hiện tại
        getActivity().startActivity(intent); // Khởi động lại hoạt động
    }
    private void setLocal(Context context, String langCode){
        Locale locale = new Locale(langCode);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}