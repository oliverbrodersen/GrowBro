package com.example.growbro.Login;

import android.util.Log;

import com.example.growbro.Data.GrowBroApi;
import com.example.growbro.Data.ServiceGenerator;
import com.example.growbro.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAO {
    private static UserDAO instance;
    private User currentUser;

    public UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public void apiAddUser(User user){
        currentUser = new User(user.getUserId(), user.getUserName(), user.getPassword()); //Dummy Data
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("User", user);
            Call<Integer> call = growBroApi.addUser(paramObject.toString());
            call.enqueue(
                    new Callback<Integer>(){
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.code() == 200){
                                int userId = response.body();
                                currentUser = new User(userId, user.getUserName(), user.getPassword());
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.e("Api error", t.toString());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
