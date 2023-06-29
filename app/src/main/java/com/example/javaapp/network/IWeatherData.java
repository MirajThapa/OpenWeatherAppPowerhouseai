package com.example.javaapp.network;

import com.example.javaapp.model.WeatherData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface IWeatherData {
    @GET("data/2.5/weather")
    Observable<WeatherData> getData(@Query("lat") double lat, @Query("lon") double lon , @Query("appid") String api);
}
