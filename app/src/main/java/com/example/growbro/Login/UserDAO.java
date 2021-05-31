package com.example.growbro.Login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Data.GrowBroApi;
import com.example.growbro.Data.ServiceGenerator;
import com.example.growbro.Models.Data.ApiResponseId;
import com.example.growbro.Models.User;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAO {
    private static UserDAO instance;
    private MutableLiveData<User> currentUser;

    public UserDAO() {
        currentUser = new MutableLiveData<>();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public void apiAddUser(User user) {
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0

        Log.i("Api json", new Gson().toJson(user));
        Call<ApiResponseId> call = growBroApi.addUser(new Gson().toJson(user));
        call.enqueue(
                new Callback<ApiResponseId>(){
                    @Override
                    public void onResponse(Call<ApiResponseId> call, Response<ApiResponseId> response) {
                        Log.i("Api response", response.toString());
                        if (response.code() == 400) {
                            try {
                                Log.i("Api 400", response.errorBody().string());
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                        if (response.code() == 200){
                            //int userId = response.body();
                            Log.i("Api response", response.body().toString());
                            currentUser.postValue(new User(response.body().getValue(), user.getUsername(), user.getPassword()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseId> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void signOut() {
        currentUser.setValue(null);
    }

    public void apiSignIn(String userName, String password) {
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
            Call<User> call = growBroApi.login(userName, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        currentUser.postValue(response.body());
                        Log.i("Login", response.body().toString() + "");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Api error", t.toString());
                }
            });
    }
}
