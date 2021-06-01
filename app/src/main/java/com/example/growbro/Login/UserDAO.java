package com.example.growbro.Login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Data.GrowBroApi;
import com.example.growbro.Data.ServiceGenerator;
import com.example.growbro.Models.Data.ApiResponseId;
import com.example.growbro.Models.User;

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

        Call<ApiResponseId> call = growBroApi.addUser(user);
        call.enqueue(
                new Callback<ApiResponseId>(){
                    @Override
                    public void onResponse(Call<ApiResponseId> call, Response<ApiResponseId> response) {
                        if (response.code() == 200){
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
