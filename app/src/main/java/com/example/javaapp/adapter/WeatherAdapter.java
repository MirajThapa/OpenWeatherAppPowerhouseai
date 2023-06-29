package com.example.javaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapp.R;
import com.example.javaapp.model.WeatherData;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter {

    List<WeatherData> weatherData;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_city_weather,parent,false);
        return new WeatherVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WeatherVH bookingVH = (WeatherVH) holder;
        bookingVH.onBind(weatherData.get(position));
    }

    @Override
    public int getItemCount() {
        if(weatherData != null){
            return weatherData.size();
        }
        else {
            return 0;
        }
    }

    public void updateData(List<WeatherData> weatherData){
        this.weatherData = weatherData;
        notifyDataSetChanged();
    }

}
