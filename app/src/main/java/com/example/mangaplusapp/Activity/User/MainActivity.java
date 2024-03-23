package com.example.mangaplusapp.Activity.User;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.Admin.DashBoardAdminActivity;
import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.Activity.Base.LoginActivity;
import com.example.mangaplusapp.Fragment.CreatorFragment;
import com.example.mangaplusapp.Fragment.FavoriteFragment;
import com.example.mangaplusapp.Fragment.HomeFragment;
import com.example.mangaplusapp.Fragment.HotFragment;
import com.example.mangaplusapp.Fragment.MangaListFragment;
import com.example.mangaplusapp.Fragment.RegionFragment;
import com.example.mangaplusapp.Fragment.SearchFragment;
import com.example.mangaplusapp.Fragment.UserProfileFragment;
import com.example.mangaplusapp.Helper.DBHelper.UserDBHelper;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity {
    private static SharedPreferences sharedPreferences;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    TextView userNameTxt,NameApp;
    String userName;
    ImageView imgViewUser,headerBackgroundLinear;
    UserDBHelper dbHelper;
    String userID,userEmail;
    FirebaseAuth mAuth ;
    FirebaseUser currentUser ;
    int textColor,iconColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // set content phải trước focus nha
        loadFragment(new HomeFragment(),false, R.menu.home_fragment_header_menu);
        sharedPreferences = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userID = currentUser.getProviderId();
        userEmail=currentUser.getEmail();
        userName = currentUser.getDisplayName();
        BackToProfile();
        focusFragment();
        loadMenuDrawer();
        navToDrawerMenuBottom();
    }
    private void BackToProfile()
    {
        int getData=getIntent().getIntExtra("BackToProfile",0);
        if(getData==1)
        {
            loadFragment(new UserProfileFragment(), false, R.menu.library_fragment_header_menu);
        }
    }
    private boolean checkAdmin(String userEmail){
        if(userEmail.equals("softwaretestact@gmail.com")){
            return true;
        }
        else{
            return false;
        }
    }
    private void setInfo(){
        dbHelper = new UserDBHelper(this);
        navigationView = findViewById(R.id.navigation_drawer_container);
        userNameTxt = navigationView.getHeaderView(0).findViewById(R.id.menu_drawer_header_username);
        imgViewUser=  navigationView.getHeaderView(0).findViewById(R.id.menu_drawer_header_image_user);
        headerBackgroundLinear=findViewById(R.id.Background_Linear_main);
        Uri imgUser = currentUser.getPhotoUrl();
        userNameTxt.setText(userName);
        Glide.with(this).load(imgUser).into(imgViewUser);
    }
    private void userAuthorization(){
        Menu menu = navigationView.getMenu();
        MenuItem spaceItem = menu.findItem(R.id.customItem);
        MenuItem spaceItem2= menu.findItem(R.id.customItem2);
        if(!checkAdmin(userEmail)){
            MenuItem menuItem = menu.findItem(R.id.adminPlace_container);
            if (menuItem != null) {
                menu.removeItem(menuItem.getItemId());
                spaceItem.setVisible(true);
                spaceItem2.setVisible(true);
            }
        }else{
            spaceItem.setVisible(false);
            spaceItem2.setVisible(false);
        }
    }
    private void navToDrawerMenuBottom(){
        navigationView=findViewById(R.id.navigation_drawer_container);
        // check admin into create layout place for admin
        userAuthorization();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.adminPlace) {
                    Intent intent = new Intent(MainActivity.this, DashBoardAdminActivity.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.menu_drawer_favorite){
                    MangaListFragment dialogFragment = new MangaListFragment();
                    Bundle args = new Bundle();
                    args.putString("tag", "Favorite");
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getSupportFragmentManager(), "Favorite");
                } else if (itemId == R.id.menu_drawer_bought) {
                    MangaListFragment dialogFragment = new MangaListFragment();
                    Bundle args = new Bundle();
                    args.putString("tag", "Bought");
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getSupportFragmentManager(), "Bought");
                } else if (itemId==R.id.Logout) {
                    if(isLoggedIn()){
                        signOut();
                    }
                    else
                    {
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else if (itemId==R.id.menu_drawer_support1) {
                    openFacebookPage();
                    
                } else if (itemId==R.id.menu_drawer_support2) {
                    openWebPage();
                }
                return true;
            }
        });
    }
    private void openFacebookPage() {
        String facebookUrl = "https://www.facebook.com/HUFLITConfessions";
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookUrl));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(intent);
        }
    }
    private void openWebPage() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.huflitconfessions.com/?fbclid=IwAR00-x8kYMBq0DxyhxYYzsOXKKINVCpl_2oJhwlHu3xbApZaCxFZ4XbzNT8"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isLoggedIn(){
        return currentUser != null;
    }
    public void signOut(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Đăng xuất khỏi Google
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Log out successful", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putBoolean("keyBiometric",false);
                                editor.apply();
                                editor.commit();
                                // Đăng xuất thành công khỏi cả Firebase và Google
                                Log.d("@@@@@", "signOut: Google");
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Xảy ra lỗi khi đăng xuất khỏi Google
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {

            // Đăng xuất khỏi Firebase Auth
            Toast.makeText(MainActivity.this, "Log out successful", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("keyBiometric",false);
            editor.apply();
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private void updateBottomNavigationView() {

        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if (backStackEntryCount > 0) {
            // There are fragments in the back stack, get the top one
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            String fragmentTag = backEntry.getName();

            // Update Bottom Navigation View based on the fragment tag
            if (fragmentTag != null) {
                switch (fragmentTag) {
                    case "HomeFragment":
                        bottomNavigationView.getMenu().findItem(R.id.navHome).setChecked(true);
                        break;
                    case "HotFragment":
                        bottomNavigationView.getMenu().findItem(R.id.navHot).setChecked(true);
                        break;
                    case "UserProfileFragment":
                        bottomNavigationView.getMenu().findItem(R.id.navProfile).setChecked(true);
                        break;
                    case "SearchFragment":
                        bottomNavigationView.getMenu().findItem(R.id.navSearch).setChecked(true);
                        break;
                    // Add more cases for other fragments
                }
            }
        } else {
            // No fragments in the back stack, set the default item
            bottomNavigationView.getMenu().findItem(R.id.navHome).setChecked(true);
        }
    }
    private void focusFragment() { // ham chay ra main cua fragment
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navigationView = findViewById(R.id.navigation_drawer_container);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        NameApp=findViewById(R.id.AppName_Title);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                //Load Fragment and Menu
                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false, R.menu.home_fragment_header_menu);
                    SwapColorBottom(R.color.Home_Bottom);
                } else if (itemId == R.id.navHot) {
                    loadFragment(new HotFragment(), false, R.menu.hot_fragment_header_menu);
                    SwapColorBottom(R.color.Hot_Bottom);
                } else if (itemId == R.id.navSearch) {
                    loadFragment(new SearchFragment(), false, R.menu.search_fragment_header_menu);
                    SwapColorBottom(R.color.Search_Bottom);
                } else if (itemId == R.id.navFavorite) {
                    loadFragment(new FavoriteFragment(), false, R.menu.creator_fragment_header_menu);
                    SwapColorBottom(R.color.Favorite_Bottom);
                } else if (itemId == R.id.navProfile) {
                    loadFragment(new UserProfileFragment(), false, R.menu.library_fragment_header_menu);
                    SwapColorBottom(R.color.Profile_Bottom);
                }
                return true;
            }
        });
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateBottomNavigationView();
            }
        });
    }
    private void SwapColorBottom(int idColor)
    {
        textColor = ContextCompat.getColor(MainActivity.this, idColor);
        iconColor = ContextCompat.getColor(MainActivity.this, idColor);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(textColor));
        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(iconColor));
        NameApp.setTextColor(iconColor);
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized,int menuResId) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()); // Ad tag to Stack with the same name of Fragment
        fragmentTransaction.commit();

        // Set the toolbar menu for the current fragment
        binding.mainHeader.getMenu().clear();
        getMenuInflater().inflate(menuResId, binding.mainHeader.getMenu());
    }
    private void loadMenuDrawer(){
        toolbar = (Toolbar) findViewById(R.id.main_header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_container);
        navigationView.bringToFront();
        drawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setInfo();
    }
    /*Override Function*/
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount(); // Take Length of Stack
            //Take Fragment below first Fragment of stack
            FragmentManager.BackStackEntry backEnd = fragmentManager.getBackStackEntryAt(backStackEntryCount - 2);
            String fragmentTag = backEnd.getName();
            //Hook Fragment with fragmentTag
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

            // Update the toolbar menu for the current fragment
            if (currentFragment instanceof HomeFragment) {
                binding.mainHeader.getMenu().clear();
                getMenuInflater().inflate(R.menu.home_fragment_header_menu, binding.mainHeader.getMenu());
            } else if (currentFragment instanceof HotFragment) {
                binding.mainHeader.getMenu().clear();
                getMenuInflater().inflate(R.menu.hot_fragment_header_menu, binding.mainHeader.getMenu());
            } else if (currentFragment instanceof SearchFragment){
                binding.mainHeader.getMenu().clear();
                getMenuInflater().inflate(R.menu.search_fragment_header_menu, binding.mainHeader.getMenu());
            }else if (currentFragment instanceof CreatorFragment){
                binding.mainHeader.getMenu().clear();
                getMenuInflater().inflate(R.menu.creator_fragment_header_menu, binding.mainHeader.getMenu());
            }else{
                binding.mainHeader.getMenu().clear();
                getMenuInflater().inflate(R.menu.library_fragment_header_menu, binding.mainHeader.getMenu());
            }
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home_fm_search) {
            // Handle action for HomeFragment
            loadFragment(new SearchFragment(),false, R.menu.search_fragment_header_menu);
            return true;
        }
        //If you wanna more feature add more condition
        return super.onOptionsItemSelected(item);
    }
}