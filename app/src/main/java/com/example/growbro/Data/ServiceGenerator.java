package com.example.growbro.Data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("IDK")
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = retrofitBuilder.build();

    private static GrowBroApi growBroApi = retrofit.create(GrowBroApi.class);

    public static GrowBroApi getGrowBroApi() {
        return growBroApi;
    }
}
