package com.example.javaapp;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.javaapp.constants.PreferencesConstants;

public class App extends Application {
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(PreferencesConstants.PREF_FILE, MODE_PRIVATE);
    }

    public static SharedPreferences getAppPreference(){
        return sharedPreferences;
    }

}