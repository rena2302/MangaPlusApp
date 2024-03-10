package com.example.mangaplusapp.Helper.ActionHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

public class KeyBoardHelper {
    @SuppressLint("ClickableViewAccessibility")
    public static void ActionRemoveKeyBoardForFragment(View mainLayout, Context context) {
        mainLayout.setOnTouchListener((v, event) -> {
            KeyBoardHelper.hideKeyboardFromFragment(context, v);
            return false;
        });

    }
    @SuppressLint("ClickableViewAccessibility")
    public static void ActionRemoveKeyBoardForActivity(View mainLayout, Activity activity)
    {
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardHelper.hideKeyboard(activity);
                return false;
            }
        });
    }
    private static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}