package com.example.javaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.javaapp.adapter.WeatherAdapter;
import com.example.javaapp.databinding.ActivityMainBinding;
import com.example.javaapp.model.Coord;
import com.example.javaapp.model.WeatherData;
import com.example.javaapp.preferences.PreferenceHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    ActivityMainBinding activityMainBinding;
    MainActivityViewModel mainActivityViewModel;
    WeatherAdapter weatherAdapter;
    ArrayList<Coord> coordinateList = new ArrayList<>();
    boolean internetAvailable = false;
    private double myLat, myLon;

    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setRecyclerView();

        mainActivityViewModel.getAllWeatherData().observe(this, new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(List<WeatherData> weatherData) {
                internetAvailable = true;
                PreferenceHelper.saveWeatherData(weatherData);
                weatherAdapter.updateData(PreferenceHelper.getAllWeatherData());
            }
        });

        if (!internetAvailable){
            if (PreferenceHelper.getAllWeatherData() != null){
                weatherAdapter.updateData(PreferenceHelper.getAllWeatherData());
                Toast.makeText(MainActivity.this,"Displaying Previous Records",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Log.i("TAG", "getLocationPermission: ");
        }

    }

    public void initializeCoordinates(double lat, double lng) {

        coordinateList.add(new Coord(lat,lng)); // My device
        coordinateList.add(new Coord(40.7128,-74.0060)); // New York
        coordinateList.add(new Coord(1.352083,103.819839)); // Singapore
        coordinateList.add(new Coord(19.075983,72.877655)); // Mumbai
        coordinateList.add(new Coord(28.704060,77.102493)); // Delhi
        coordinateList.add(new Coord(-33.8651,151.2099)); // Sydney
        coordinateList.add(new Coord(-37.8136,144.9631)); // Melbourne
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        weatherAdapter = new WeatherAdapter();
        activityMainBinding.rvMainActivity.setLayoutManager(layoutManager);
        activityMainBinding.rvMainActivity.setAdapter(weatherAdapter);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void getDeviceLocation() {
        try {
            if (checkPermissions()) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(1000); // Update interval in milliseconds

                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Location location = locationResult.getLastLocation();
                            if (location != null) {
                                myLat = location.getLatitude();
                                myLon = location.getLongitude();
                                Toast.makeText(MainActivity.this, myLat + ", " + myLon, Toast.LENGTH_SHORT).show();

                                initializeCoordinates(myLat, myLon);
                                for (int i = 0; i < coordinateList.size(); i++) {
                                    mainActivityViewModel.fetchWeatherData(coordinateList.get(i));
                                }
                                fusedLocationProviderClient.removeLocationUpdates(this); // Stop receiving location updates
                            }
                        }
                    }
                };

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private boolean checkPermissions() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }




}