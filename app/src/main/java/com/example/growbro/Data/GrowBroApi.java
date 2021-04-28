package com.example.growbro.Data;

import com.example.growbro.Models.Data.ApiCurrentDataPackage;
import com.example.growbro.Models.Data.ApiReceipt;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GrowBroApi {
    @GET("/User/{userId}/Greenhouse/{greenhouseId}/CurrentData")
    Call<String> getDummyData(@Path("userId") int userId, @Path("greenhouseId") int greenhouseId);

    @Headers("Content-Type: application/json")
    @GET("/user")
    Call<User> login(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("/user")
    Call<ApiReceipt> addUser(@Body String body);

    @GET("/user/{userId}/greenhouse")
    Call<List<Greenhouse>> getGreenhouseList(@Path("userId") int userId);

    @GET("/user/{userId}/greenhouse/{greenhouseId}")
    Call<Greenhouse> getGreenhouse(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @GET("/user/{userId}/greenhouse/{greenhouseId}/currentData")
    Call<ApiCurrentDataPackage> getCurrentData(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @Headers("Content-Type: application/json")
    @GET("user/{userId}/greenhouse/{greenhouseId}/averageData")
    Call<ApiCurrentDataPackage> getAverageData(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId, @Body String body);

    @POST("/user/{userId}/greenhouse/{greenhouseId}/waternow")
    Call<ApiReceipt> waterNow(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @POST("/user/{userId}/greenhouse/{greenhouseId}/openWindow")
    Call<ApiReceipt> openWindow(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @Headers("Content-Type: application/json")
    @GET("/user/{userId}/addGreenhouse")
    Call<Integer> addGreenhouse(@Path("userId")int userId, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("/user/{userId}/greenhouse/{greenhouseId}/plant")
    Call<Integer> addPlant(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId, @Body String body);

}
