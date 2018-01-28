package com.signup;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.signup.base.BaseFragment;
import com.signup.base.DrawerItemBaseFragment;

import java.util.ArrayList;
import java.util.List;



import com.signup.base.HomeInterface;
import com.signup.databinding.ActivityMainBinding;
import com.signup.frags.Frag2;
import com.signup.frags.HomeFragment;
import com.signup.frags.SignUpFrag;
import com.signup.model.DrawerItem;
import com.signup.model.login.AppUser;
import com.signup.utils.AppPref;

public class MainActivity extends AppCompatActivity implements HomeInterface ,View.OnClickListener {
    ActivityMainBinding activityMainBinding;
    List<DrawerItem> drawerItems;
    ActionBarDrawerToggle actionBarDrawerToggle;
    BaseFragment selectedFragment;
    DrawerItemBaseFragment drawerItemBaseFragment;

    private boolean isWarnedToClose = false, isDrawerLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerItems = new ArrayList<>();

        drawerItems.add(new DrawerItem(getString(R.string.home), R.drawable.ic_menu_home));
        drawerItems.add(new DrawerItem(getString(R.string.login_signup), R.drawable.ic_menu_user));
        drawerItems.add(new DrawerItem(getString(R.string.favorites), R.drawable.ic_menu_star));
        drawerItems.add(new DrawerItem(getString(R.string.about_us), R.drawable.ic_menu_about));
        drawerItems.add(new DrawerItem(getString(R.string.faqs), R.drawable.ic_menu_faq));
        drawerItems.add(new DrawerItem(getString(R.string.contact_us), R.drawable.ic_menu_email));
        drawerItems.add(new DrawerItem(getString(R.string.setting), R.drawable.ic_menu_settings));


        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        activityMainBinding.executePendingBindings();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setClickListener();

        activityMainBinding.setMenus(drawerItems);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUserUI();
                if (drawerItemBaseFragment == null) {
                    Log.d("fragment", "is null");
                    showDrawerItemFragment(new HomeFragment());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   getAboutUs();
                        }
                    }, 500);

                }

            }
        }, 500);


    }


    private void setClickListener() {

        activityMainBinding.laytHome.laytMenu.setOnClickListener(this);
        activityMainBinding.laytAbout.laytMenu.setOnClickListener(this);
        activityMainBinding.laytContact.laytMenu.setOnClickListener(this);
        activityMainBinding.laytFaq.laytMenu.setOnClickListener(this);
        activityMainBinding.laytFav.laytMenu.setOnClickListener(this);
        activityMainBinding.laytLogin.laytMenu.setOnClickListener(this);
        activityMainBinding.laytSetting.laytMenu.setOnClickListener(this);


    }


    @Override
    public void setSelectedDrawerItem(DrawerItemBaseFragment fragment) {
        this.drawerItemBaseFragment = fragment;
    }

    @Override
    public void setSelectedFragment(BaseFragment fragment) {
        this.selectedFragment = fragment;
        if (selectedFragment instanceof DrawerItemBaseFragment) {
            // If foreground fragment is drawer item, unlock drawer
            unlockDrawer();
        } else {
            // If foreground fragment is not drawer item, lock drawer
            lockDrawer();
        }
    }

    private void lockDrawer() {
        if (isDrawerLocked) {
            isDrawerLocked = false;
            activityMainBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void unlockDrawer() {
        if (!isDrawerLocked) {
            isDrawerLocked = true;
            activityMainBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    @Override
    public void popBackStack() {
        try {
            getSupportFragmentManager().popBackStackImmediate();
            hideBoard();
        } catch (Exception e) {

        }

    }

    @Override
    public void popBackStackTillTag(String tag) {
        try {
            getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            hideBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean withAnimation) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
/**
 if (withAnimation) {
 // TO ENABLE FRAGMENT ANIMATION
 // Format: setCustomAnimations(old_frag_exit, new_frag_enter, old_frag_enter, new_frag_exit);
 if (fragment instanceof SocialShareFrag || fragment instanceof CategoryFrag || fragment instanceof RegionFrag) {
 ft.setCustomAnimations(R.anim.slide_in_top, 0, 0, R.anim.slide_out_top);
 } else if (fragment instanceof ReviewsFrag || fragment instanceof SortFrag || fragment instanceof FilterFrag) {
 ft.setCustomAnimations(R.anim.slide_in_bottom, 0, 0, R.anim.slide_out_bottom);
 } else {
 Configuration config = getResources().getConfiguration();
 if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
 //in Right To Left layout
 ft.setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right, R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left);
 } else {
 ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);

 }

 }
 }
 **/

if(withAnimation){
    ft.setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right, R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left);

}

        ft.replace(R.id.baseFragment, fragment, fragment.getTagText());
        ft.addToBackStack(fragment.getTagText());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void addMultipleFragments(BaseFragment[] fragments) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Record all steps for the transaction.
        for (BaseFragment fragment : fragments) {
            //ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
            ft.replace(R.id.baseFragment, fragment, fragment.getTagText());
        }

        // Add the transaction to backStack with tag of first added fragment
        ft.addToBackStack(fragments[0].getTagText());

        // Commit the transaction.
        ft.commitAllowingStateLoss();
    }

    @Override
    public void showDrawerItemFragment(DrawerItemBaseFragment fragment) {
        // Clear backstack if app is not at a first-tier fragment
        // and drawer is not locked, so that app could be switched between
        // any first-tier fragment from anywhere.
        try {
            if (!(selectedFragment instanceof DrawerItemBaseFragment) && !isDrawerLocked) {
                clearBackStack();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.baseFragment, fragment, fragment.getTagText());
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d("cleared", fragmentManager.getBackStackEntryCount() + " fragment");
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();

        }
    }

    @Override
    public void closeDrawer() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void openDrawer() {
        activityMainBinding.drawerLayout.openDrawer(GravityCompat.START);

        hideBoard();
    }

    @Override
    public void hideBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserUI() {

    }

    @Override
    public Location getMyLocation() {
        return null;
    }

    @Override
    public void resetLocation() {

    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void setFavCount(int count, boolean canReset) {

    }

    @BindingAdapter("imageSrc")
    public static void setMenuIcon(ImageView imageView, int resId) {
        try {
            imageView.setImageResource(resId);

        } catch (Exception e) {

        }
    }

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        try {
            if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (selectedFragment != null && !selectedFragment.onBackPressed()) {
                    // If not consumed, handle it.
                    super.onBackPressed();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }

    private void setDrawerFrag(final DrawerItemBaseFragment drawerFrag) {
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                showDrawerItemFragment(drawerFrag);
            }
        }, 250);
    }

    @Override
    public void onClick(View v) {
        try {


            if (v == activityMainBinding.laytHome.laytMenu) {
                if (!(drawerItemBaseFragment instanceof HomeFragment)) {
                    setDrawerFrag(new HomeFragment());
                }
            } else if (v == activityMainBinding.laytLogin.laytMenu) {

                Toast.makeText(getApplicationContext(),"frag2",Toast.LENGTH_SHORT).show();
                addFragment(new SignUpFrag(),true);


            } else if (v == activityMainBinding.laytFaq.laytMenu) {

            }

            closeDrawer();
        } catch (Exception e) {

        }
    }
    public  static AppUser appUser;
    @Override
    public AppUser getUser() {
        AppUser user = null;
        if (AppPref.getUserDataFromPreferences()!=null){
            appUser = AppPref.getUserDataFromPreferences().getAppUser();
            return appUser;
        }

        return user;
    }
}