package com.example.javaapp.preferences;

import android.content.SharedPreferences;

import com.example.javaapp.App;
import com.example.javaapp.constants.PreferencesConstants;
import com.example.javaapp.model.WeatherData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

public class PreferenceHelper {
    public static void saveWeatherData(List<WeatherData> weatherData){
        SharedPreferences.Editor editor = App.getAppPreference().edit();
        editor.putString(PreferencesConstants.WEATHER_DATA,new GsonBuilder().create().toJson(weatherData));
        editor.apply();
    }

    public static List<WeatherData> getAllWeatherData() {
        SharedPreferences sharedPreferences = App.getAppPreference();
        String data = sharedPreferences.getString(PreferencesConstants.WEATHER_DATA, "");
        List<WeatherData> weatherDataList = new Gson().fromJson(data, new TypeToken<List<WeatherData>>(){}.getType());
        return weatherDataList;
    }

    public static void saveDateTime(Date dateTime){
        SharedPreferences.Editor editor = App.getAppPreference().edit();
        editor.putString(PreferencesConstants.DATE_TIME, String.valueOf(dateTime));
        editor.apply();
    }

    public static String getDateTime() {
        SharedPreferences sharedPreferences = App.getAppPreference();
        String data = sharedPreferences.getString(PreferencesConstants.DATE_TIME, "");
        return data;
    }
}
