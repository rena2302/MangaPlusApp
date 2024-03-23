package com.example.mangaplusapp.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mangaplusapp.Fragment.HotBoughtFragment;
import com.example.mangaplusapp.Fragment.HotViewFragment;
import com.example.mangaplusapp.R;

public class HotAdapter extends FragmentStatePagerAdapter {

    Context context;
    public HotAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    public void setContext(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HotViewFragment();
            case 1: return new HotBoughtFragment();
            default: return new HotViewFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                String viewCount = context.getString(R.string.viewHot);
                title = viewCount;
                break;
            case 1:
                String buyCount = context.getString(R.string.boughtHot);
                title = buyCount;
                break;
        }
        return title;
    }
}
