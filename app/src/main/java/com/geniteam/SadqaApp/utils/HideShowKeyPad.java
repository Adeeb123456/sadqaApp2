package com.geniteam.SadqaApp.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class HideShowKeyPad {


    public void setupUI(View obj) {

        if (!(obj instanceof EditText)) {

            obj.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (v.getContext() instanceof Activity) {
                        hideSoftKeyboard((Activity) v.getContext());
                        return false;
                    }

                    return false;
                }

            });
        }

        if (obj instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) obj).getChildCount(); i++) {

                View innerView = ((ViewGroup) obj).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    private void hideSoftKeyboard(Activity context) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        try {
            if (context != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (context.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(
                            context.getCurrentFocus()
                                    .getWindowToken(), 0);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }
}
