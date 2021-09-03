package com.bohrapankaj.interviewtaskjava.network;

import com.bohrapankaj.interviewtaskjava.model.JsonModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("18IC")
    Call<ArrayList<JsonModel>> getDetail();
}
