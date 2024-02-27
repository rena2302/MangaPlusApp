package Helper.ActionHelper;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mangaplusapp.R;
import com.example.mangaplusapp.RegisterActivity;

public class KeyBoardHelper {
    public static void ActionRemoveKeyBoardForFragment(Context context, ViewGroup container, LayoutInflater inflater, int ID) {
        View mainLayout =inflater.inflate(ID, container, false);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardHelper.hideKeyboardFromFragment(context, v);
                return false;
            }
        });
//        // Gỡ bỏ touch listener khi Fragment không còn hiển thị
//        final FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//        View.OnTouchListener finalTouchListener = touchListener;
//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                Fragment currentFragment = fragmentManager.findFragmentById(container.getId());
//                if (currentFragment != null && currentFragment.isVisible() && currentFragment.getView() == mainLayout) {
//                    mainLayout.setOnTouchListener(finalTouchListener);
//                } else {
//                    mainLayout.setOnTouchListener(null); // Gỡ bỏ touch listener
//                }
//            }
//        });
    }
    public static void ActionRemoveKeyBoardForActivity(View mainLayout,Activity activity)
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
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
public class KeyBoardHelper {
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
