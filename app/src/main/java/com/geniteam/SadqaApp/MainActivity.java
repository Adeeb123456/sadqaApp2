package com.geniteam.SadqaApp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.geniteam.SadqaApp.base.BaseFragment;
import com.geniteam.SadqaApp.base.DrawerItemBaseFragment;

import java.util.ArrayList;
import java.util.List;


import com.geniteam.SadqaApp.base.HomeInterface;
import com.geniteam.SadqaApp.databinding.ActivityMainBinding;
import com.geniteam.SadqaApp.frags.HomeFragment;
import com.geniteam.SadqaApp.frags.LoginFragment;
import com.geniteam.SadqaApp.frags.ProfileFrag;
import com.geniteam.SadqaApp.frags.SignUpFrag;
import com.geniteam.SadqaApp.frags.StartFragment;
import com.geniteam.SadqaApp.model.DrawerItem;
import com.geniteam.SadqaApp.model.login.AppUser;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.utils.CommonUtils;
import com.geniteam.SadqaApp.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements HomeInterface, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ActivityMainBinding activityMainBinding;
    List<DrawerItem> drawerItems;
    ActionBarDrawerToggle actionBarDrawerToggle;
    BaseFragment selectedFragment;
    DrawerItemBaseFragment drawerItemBaseFragment;

    private boolean isWarnedToClose = false, isDrawerLocked = false;


    //
    //fused location
    public static String[] locationPermission = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 4;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    protected boolean mRequestingLocationUpdatesBool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerItems = new ArrayList<>();

        drawerItems.add(new DrawerItem(getString(R.string.home), R.mipmap.ic_launcher_round));
        drawerItems.add(new DrawerItem(getString(R.string.login_signup), R.mipmap.ic_launcher_round));
        // drawerItems.add(new DrawerItem(getString(R.string.favorites), R.drawable.ic_menu_star));
        drawerItems.add(new DrawerItem(getString(R.string.about_us), R.mipmap.ic_launcher_round));
        drawerItems.add(new DrawerItem(getString(R.string.faqs), R.mipmap.ic_launcher_round));
        drawerItems.add(new DrawerItem(getString(R.string.contact_us), R.mipmap.ic_launcher_round));
        drawerItems.add(new DrawerItem(getString(R.string.setting), R.mipmap.ic_launcher_round));


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
                setUpDrawerUi(getUser());
                if (drawerItemBaseFragment == null) {
                    Log.d("fragment", "is null");
                    if (AppBase.mFirebaseAuth.getCurrentUser() == null && getUser() == null) {
                        addFragment(new StartFragment(), true);
                    } else {
                        showDrawerItemFragment(new HomeFragment());

                    }


                }

            }
        }, 500);

        CommonUtils.printHashKey(this);
        if (CommonUtils.isMyGpsEnable(getApplicationContext())) {

            buildGoogleApiClient();

        } else {
            Utils.showAlertDialogue(this, "", "please enable Gps to use our service", new Utils.AlertDialogueClickListener() {
                @Override
                public void onOkClicked(int position) {
                    CommonUtils.enableGPs(MainActivity.this);

                }
            });


        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdatesBool) {
            startLocationUpdates();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

    }

    private void setClickListener() {

        activityMainBinding.laytHome.laytMenu.setOnClickListener(this);
        activityMainBinding.laytAbout.laytMenu.setOnClickListener(this);
        activityMainBinding.laytContact.laytMenu.setOnClickListener(this);
        activityMainBinding.laytFaq.laytMenu.setOnClickListener(this);
        activityMainBinding.laytFav.laytMenu.setOnClickListener(this);
        activityMainBinding.laytLogin.laytMenu.setOnClickListener(this);
        activityMainBinding.laytSetting.laytMenu.setOnClickListener(this);
        activityMainBinding.laytLogout.laytMenu.setOnClickListener(this);


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

    @Override
    public void setUpDrawerUi(AppUser appUser) {
        try {

            if (appUser != null) {
                if (drawerItems.size() == 6) {
                    drawerItems.get(1).setTitle("User Profile");

                    drawerItems.add(new DrawerItem(getString(R.string.log_out), R.mipmap.ic_launcher_round));

                }
            } else {
                drawerItems.get(1).setTitle(getString(R.string.login_signup));
                if (drawerItems.size() == 7) {
                    drawerItems.remove(6);
                }

            }
        } catch (Exception e) {

        }

        try {
            activityMainBinding.setMenus(drawerItems);

            if (appUser != null) {
                // activityMainBinding.navView.findViewById(R.id.ivLogo).setVisibility(View.GONE);
                // activityMainBinding.navView.findViewById(R.id.laytUserDetail).setVisibility(View.VISIBLE);
                activityMainBinding.laytHeader.tvUserName.setText(appUser.getUFullName());

                ProfileFrag.setProfileImage(activityMainBinding.laytHeader.ivPicture, appUser.getUImageURL().toString());
            } else {
                activityMainBinding.laytHeader.tvUserName.setText("");
                ProfileFrag.setProfileImage(activityMainBinding.laytHeader.ivPicture, "");
                //  activityMainBinding.navView.findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
                // activityMainBinding.navView.findViewById(R.id.laytUserDetail).setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void logout() {
        Log.d("debug", "logout");
        try {
            if (getUser().getUImageURL() != null)
                Picasso.with(this).invalidate(getUser().getUImageURL().toString());
            else {
                //Picasso.with(this).invalidate(getUser().getUImageURL().toString());

            }
            appUser = null;
            AppPref.clear();
            setUpDrawerUi(appUser);
            AppBase.mFirebaseAuth.signOut();
            setDrawerFrag(new LoginFragment());
        } catch (Exception e) {
            e.printStackTrace();
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

        if (withAnimation) {
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
        try {

            if (AppBase.mFirebaseAuth.getCurrentUser() != null) {
                if (drawerItems.size() == 7) {
                    drawerItems.add(new DrawerItem(getString(R.string.log_out), R.drawable.ic_menu_logout));
                }
            } else {
                drawerItems.get(1).setTitle(getString(R.string.login_signup));
                if (drawerItems.size() == 8) {
                    drawerItems.remove(7);
                }

            }
        } catch (Exception e) {

        }

        try {
            activityMainBinding.setMenus(drawerItems);

            if (AppBase.mFirebaseAuth.getCurrentUser() != null) {
                // activityMainBinding.navView.findViewById(R.id.ivLogo).setVisibility(View.GONE);
                // activityMainBinding.navView.findViewById(R.id.laytUserDetail).setVisibility(View.VISIBLE);
                activityMainBinding.laytHeader.tvUserName.setText(appUser.getUFullName());
                ProfileFrag.setProfileImage(activityMainBinding.laytHeader.ivPicture, appUser.getUImageURL().toString());
            } else {
                activityMainBinding.laytHeader.tvUserName.setText("");
                ProfileFrag.setProfileImage(activityMainBinding.laytHeader.ivPicture, "");
                //  activityMainBinding.navView.findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
                // activityMainBinding.navView.findViewById(R.id.laytUserDetail).setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public Location getMyLocation() {
        return mCurrentLocation;
    }

    @Override
    public void resetLocation() {
        mCurrentLocation = null;
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

                Toast.makeText(getApplicationContext(), "frag2", Toast.LENGTH_SHORT).show();
                addFragment(new SignUpFrag(), true);


            } else if (v == activityMainBinding.laytFaq.laytMenu) {

            } else if (v == activityMainBinding.laytLogout.laytMenu) {
                logout();
            }

            closeDrawer();
        } catch (Exception e) {

        }
    }

    public static AppUser appUser;

    @Override
    public AppUser getUser() {
        AppUser user = null;
        if (AppPref.getUserDataFromPreferences() != null) {
            appUser = AppPref.getUserDataFromPreferences();
            return appUser;
        }

        return user;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        AppBase.mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }


    //location api fused location api

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(30); //higher priority
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestForPermission(locationPermission);
                return;
            }
        }

        mRequestingLocationUpdatesBool = true;
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //fused location api callbacks

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("debug", "on connected");
        try {
            if (mCurrentLocation == null) {
                boolean canLoad = false;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        canLoad = true;
                    }

                } else {
                    canLoad = true;
                }

                if (canLoad) {
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }
            }

            startLocationUpdates();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult != null && connectionResult.getErrorMessage() != null) {
            Toast.makeText(getApplicationContext(), "Gps error: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d("debug", location.getLatitude() + " " + location.getLongitude());
    }

    private void requestForPermission(final String[] permissions) {
        ActivityCompat.requestPermissions(MainActivity.this, permissions,
                AppConstants.REQ_LOCATION);
    }


    private void addHomeFrag() {
        showDrawerItemFragment(new HomeFragment());
    }

    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

}
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("debug","permissions");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==AppConstants.REQ_LOCATION){
            if(CommonUtils.verifyPermissions(grantResults)){
                mRequestingLocationUpdatesBool=true;
                getLastKnownLocation();
                startLocationUpdates();


            }else {
                Utils.showSnackBarStatusFail(activityMainBinding.getRoot(), "Fail to get Current Location ", "Retry", new Utils.SnackBarButtonListener() {
                    @Override
                    public void onBtnClickListener() {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                boolean showRational=ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0]);
                if(!showRational){

                    // Toast.makeText(getApplicationContext(),"Please Enable your location ",Toast.LENGTH_SHORT).show();

                }

            }

        }


    }

}