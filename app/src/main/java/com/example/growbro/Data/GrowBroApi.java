package com.example.growbro.Data;

import com.example.growbro.Models.Data.ApiCurrentDataPackage;
import com.example.growbro.Models.Data.ApiReceipt;
import com.example.growbro.Models.Data.CurrentDataResultFromApi;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GrowBroApi {
    @GET("/CurrentData")
    Call<ApiCurrentDataPackage> getDummyData();

    @Headers("Content-Type: application/json")
    @GET("/user")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @Headers("Content-Type: application/json")
    @POST("/user/adduser")
    Call<Integer> addUser(@Body String body);

    @GET("/user/{userId}/greenhouse")
    Call<List<Greenhouse>> getGreenhouseList(@Path("userId") int userId);

    @GET("/user/{userId}/GetFriendsGreenhouses")
    Call<List<Greenhouse>> getFriendsGreenhouses(@Path("userId") int userId);

    @GET("/user/{userId}/greenhouse/{greenhouseId}")
    Call<Greenhouse> getGreenhouse(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @GET("/User/{userId}/Greenhouse/{greenhouseId}/CurrentData")
    Call<CurrentDataResultFromApi> getCurrentData(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @Headers("Content-Type: application/json")
    @GET("user/{userId}/greenhouse/{greenhouseId}/averageData")
    Call<ApiCurrentDataPackage> getAverageData(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId, @Body String body);

    @GET("/user/{userId}/greenhouse/{greenhouseId}/averageDataHistory") //TODO Snak med DAI om at vi nok gerne vil have sådan et endpoint her
    Call<List<ApiCurrentDataPackage>> getAverageDataHistory(@Path("userId") int userId, @Path("greenhouseId") String greenhouseId, @Body String body);

    @POST("/user/{userId}/greenhouse/{greenhouseId}/waternow")
    Call<ApiReceipt> waterNow(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @POST("/user/{userId}/greenhouse/{greenhouseId}/openWindow")
    Call<ApiReceipt> openWindow(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId);

    @Headers("Content-Type: application/json")
    @POST("/user/{userId}/addGreenhouse")
    Call<Void> addGreenhouse(@Path("userId")int userId, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("/user/{userId}/greenhouse/{greenhouseId}/plant")
    Call<Integer> addPlant(@Path("userId")int userId, @Path("greenhouseId") int greenhouseId, @Body String body);

}
