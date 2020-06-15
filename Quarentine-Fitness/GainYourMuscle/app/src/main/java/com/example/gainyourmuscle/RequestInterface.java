package com.example.gainyourmuscle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RequestInterface {
    //now  we make a GET request
    @GET("db.json")
//Car model class ko list banayera function ma declare gareko
    Call<List<GymModel>> getGymJson();
}

