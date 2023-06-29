package com.example.javaapp.network;

import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

public class SupportInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        String accessToken = "";
//        accessToken = PreferenceHelper.getLoginToken();

        request = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "max-age=60")
                .addHeader("Authorization", accessToken)
                .build();


        Response response = chain.proceed(request);
        if (response.code() == 401) {
            //Todo do unauthorized to access work
        }
        return response;
    }
}