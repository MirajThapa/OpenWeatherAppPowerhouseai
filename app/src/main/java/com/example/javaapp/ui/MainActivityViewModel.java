package com.example.javaapp.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.javaapp.model.Coord;
import com.example.javaapp.model.WeatherData;
import com.example.javaapp.network.IWeatherData;
import com.example.javaapp.network.RetrofitClient;
import com.example.javaapp.preferences.PreferenceHelper;
import com.example.javaapp.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class MainActivityViewModel extends AndroidViewModel {

    String API_KEY = "a2aa330442346839e5f4c42de23c8167";
    MutableLiveData<List<WeatherData>> mutableLiveData = new MutableLiveData<>();
    List<WeatherData> weatherDataList = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public LiveData<List<WeatherData>> getAllWeatherData(){
        return mutableLiveData;
    }

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchWeatherData(Coord coordinates){

            IWeatherData iWeatherData = RetrofitClient.getClient().create(IWeatherData.class);

            Observable<WeatherData> observable = iWeatherData.getData(coordinates.getLat(), coordinates.getLon(), API_KEY);

            observable.subscribeOn(SchedulerProvider.getInstance().io()).observeOn(SchedulerProvider.getInstance().ui()).subscribe(new Observer<WeatherData>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onNext(WeatherData weatherData) {
                    weatherDataList.add(weatherData);
                    Log.i("TAG", "onNext: "+weatherData.getMain().getTemp().toString());
                    mutableLiveData.postValue(weatherDataList);
                    weatherData.getName();

                    Date currentTime = Calendar.getInstance().getTime();
                    PreferenceHelper.saveDateTime(currentTime);
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("TAG", "onError: "+e.getLocalizedMessage());
                }

                @Override
                public void onComplete() {

                }
            });

    }

    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null){
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }



}
