package com.geniteam.SadqaApp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.geniteam.SadqaApp.model.login.AppUser;
import com.google.gson.Gson;

import java.util.prefs.Preferences;

import com.geniteam.SadqaApp.model.login.LoginModel;


/**
 * Designed for storing settings
 */
public class AppPref {

    protected static SharedPreferences prefs;
    private static final String EMPTY_STRING = "";
    private static SharedPreferences sharedPreferences;

    public static void init(Application app) {
        prefs = app.getSharedPreferences(Preferences.class.getName(),
                Context.MODE_PRIVATE);
        sharedPreferences = app.getSharedPreferences(Preferences.class.getName(),
                Context.MODE_PRIVATE);
    }

    public static void putValueByKey(String key, Object value) {
        put(key, value);
    }

    public static String getStringByKey(String key) {
        return prefs.getString(key, "");
    }

    public static String getModificationDateByKey(String key) {
        return prefs.getString(key, "0000:00:00 00:00:00");
    }

    public static Boolean getBooleanByKey(String key) {
        return prefs.getBoolean(key, false);
    }

    public static Boolean getNotificationStatus() {
        return prefs.getBoolean(AppConst.canShowNotification, true);
    }

    public static void setNotificationStatus(boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(AppConst.canShowNotification, value);
        editor.apply();
    }

    public static Integer getIntegerByKey(String key) {
        return prefs.getInt(key, -1);
    }

    protected static void put(String name, Object value) {
        SharedPreferences.Editor editor = prefs.edit();
        if (value.getClass() == Boolean.class) {
            editor.putBoolean(name, (Boolean) value);
        }
        if (value.getClass() == String.class) {
            editor.putString(name, String.valueOf(value));
        }
        if (value.getClass() == Integer.class) {
            //noinspection ConstantConditions
            editor.putInt(name, (Integer) value);
        }

        editor.apply();
    }

    public static void clear() {

        try {
            int distance = AppPref.getIntegerByKey(AppConst.gpsDistance);
            prefs.edit().clear().apply();
            sharedPreferences.edit().clear().apply();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(AppConst.gpsDistance, distance);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLanguage() {
        return prefs.getString(AppConst.appLanguage, AppConst.langEn);
    }

    public static void setLanguage(String lang) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AppConst.appLanguage, lang);
        editor.apply();
    }


    public static void saveUserModel(AppUser appUser) {
        if (appUser == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(appUser);
        setStringInPreferences(AppConst.keyUser, json.toString());
    }


    public static AppUser getUserDataFromPreferences() {
        AppUser userData = null;
        Gson gson = new Gson();
        String model = getStringFromPreferances(AppConst.keyUser);
        userData = gson.fromJson(model, AppUser.class);
        return userData;
    }

    public static String getStringFromPreferances(String key) {
        String data = sharedPreferences.getString(key, EMPTY_STRING);
        return data;
    }

    public static boolean setStringInPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

}
