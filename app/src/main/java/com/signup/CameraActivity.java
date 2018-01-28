package com.signup;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.signup.base.BaseFragment;
import com.signup.base.DrawerItemBaseFragment;
import com.signup.base.HomeInterface;
import com.signup.camerautils.CameraFragment;
import com.signup.model.login.AppUser;


/**
 * Created by USER3 on 2/18/2017.
 */

public class CameraActivity extends AppCompatActivity implements HomeInterface {

    public static final String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }*/
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }
    }

    public void returnPhotoUri(Uri uri) {
        Intent data = new Intent();
        data.setData(uri);

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }

        finish();
    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void setSelectedDrawerItem(DrawerItemBaseFragment fragment) {

    }

    @Override
    public void setSelectedFragment(BaseFragment fragment) {

    }

    @Override
    public void popBackStack() {

    }

    @Override
    public void popBackStackTillTag(String tag) {

    }

    @Override
    public void addFragment(BaseFragment fragment, boolean withAnimation) {

    }

    @Override
    public void addMultipleFragments(BaseFragment[] fragments) {

    }

    @Override
    public void showDrawerItemFragment(DrawerItemBaseFragment fragment) {

    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public void openDrawer() {

    }

    @Override
    public void hideBoard() {

    }

    @Override
    public void setUserUI() {

    }

    @Override
    public AppUser getUser() {
        return null;
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
}
