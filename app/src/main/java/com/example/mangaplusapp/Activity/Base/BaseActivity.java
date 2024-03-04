package com.example.mangaplusapp.Activity.Base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaplusapp.util.ActivityUtils;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Xử lý sự kiện khi nút back được nhấn
        // Ở đây tạm thời để nó trống, các activity con có thể override lại phương thức này nếu cần
        super.onBackPressed();
    }

    protected void startNewActivity(Class<?> newActivityClass, String... extras) {
        ActivityUtils.startNewActivity(this, newActivityClass, extras);
    }

    protected void startNewActivityAndFinishCurrent(Class<?> newActivityClass, String... extras) {
        ActivityUtils.startNewActivityAndFinishCurrent(this, newActivityClass, extras);
    }
}

