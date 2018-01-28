package com.signup.model;

/**
 * Created by guestsAll on 1/10/2018.
 */

public class DrawerItem {
    private String title;
    private int resId;

    public DrawerItem(String title,int resId) {
       this.title=title;
        this.resId=resId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }
}
