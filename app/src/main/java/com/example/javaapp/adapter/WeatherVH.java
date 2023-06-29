package com.example.javaapp.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapp.R;
import com.example.javaapp.model.WeatherData;
import com.example.javaapp.preferences.PreferenceHelper;

public class WeatherVH extends RecyclerView.ViewHolder {
    TextView temperature,humidity,pressure,cityName,date,time;
    public WeatherVH(@NonNull View itemView) {
        super(itemView);
        temperature = itemView.findViewById(R.id.temperature_value);
        humidity = itemView.findViewById(R.id.humidity_value);
        pressure = itemView.findViewById(R.id.atm_pressure_value);
        time = itemView.findViewById(R.id.weather_time);
        cityName = itemView.findViewById(R.id.weather_location);
    }


    @SuppressLint("SetTextI18n")
    public void onBind(WeatherData weatherData) {
        temperature.setText((int)(weatherData.getMain().getTemp()-273)+" C");
        humidity.setText(weatherData.getMain().getHumidity().toString());
        pressure.setText(weatherData.getMain().getPressure().toString());
        time.setText(PreferenceHelper.getDateTime());
        cityName.setText(weatherData.getName());
    }


}
