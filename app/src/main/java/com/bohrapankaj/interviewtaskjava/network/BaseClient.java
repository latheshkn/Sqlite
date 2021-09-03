package com.bohrapankaj.interviewtaskjava.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseClient {

    private static String BASE_URL = "https://jsonkeeper.com/b/";

    private static Retrofit retrofgitEndPoint = null;

    public static Retrofit getBaseClient(){
        if (retrofgitEndPoint == null){
            retrofgitEndPoint = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofgitEndPoint;
    }
}
