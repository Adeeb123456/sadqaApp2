package com.signup;

import android.app.Application;
import android.content.Context;

import java.io.File;

import com.signup.utils.AppPref;
import com.signup.webservice.ServiceGenerator;
import com.signup.webservice.WebService;


/**
 * Created by USER3 on 1/17/2017.
 */

public class AppBase extends Application {

    Context context;
    public static WebService service;
    public static File cacheDir;

    @Override
    public void onCreate() {
        super.onCreate();



        context = this;
        AppPref.init(this);
        service = ServiceGenerator.createService(WebService.class);

    }


    }







