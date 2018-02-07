package com.geniteam.SadqaApp;

import android.app.Application;
import android.content.Context;

import java.io.File;

import com.facebook.CallbackManager;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.webservice.ServiceGenerator;
import com.geniteam.SadqaApp.webservice.WebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * Created by USER3 on 1/17/2017.
 */

public class AppBase extends Application {

    Context context;
    public static WebService service;
    public static File cacheDir;
    public static FirebaseAuth mFirebaseAuth;
    public static DatabaseReference mFireBaseDbReference;
    public static StorageReference mFirebaseStorage;

    public static CallbackManager mCallbackManager;


    @Override
    public void onCreate() {
        super.onCreate();



        context = this;
        AppPref.init(this);
        service = ServiceGenerator.createService(WebService.class);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFireBaseDbReference= FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage= FirebaseStorage.getInstance().getReference();

        //facebook
        mCallbackManager = CallbackManager.Factory.create();

    }


    }







