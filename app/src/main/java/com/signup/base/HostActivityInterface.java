package com.signup.base;

import android.location.Location;

import com.signup.model.login.AppUser;


public interface HostActivityInterface {
    public void setSelectedFragment(BaseFragment fragment);

    public void popBackStack();

    public void popBackStackTillTag(String tag);

    public void addFragment(BaseFragment fragment, boolean withAnimation);

    public void addMultipleFragments(BaseFragment fragments[]);

    public void showDrawerItemFragment(DrawerItemBaseFragment fragment);

    public void closeDrawer();

    public void openDrawer();

    public void hideBoard();

    public void setUserUI();

    public AppUser getUser();

    public Location getMyLocation();

    public void resetLocation();

    public void requestPermission();

    public void setFavCount(int count, boolean canReset);
}