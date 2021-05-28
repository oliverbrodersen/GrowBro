package com.example.growbro.Login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Data.GrowBroApi;
import com.example.growbro.Data.ServiceGenerator;
import com.example.growbro.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAO {
    private static UserDAO instance;
    private User currentUser;
    private MutableLiveData<ArrayList<User>> friendList;

    public UserDAO() {
        friendList = new MutableLiveData<>();
        friendList.setValue(new ArrayList<User>());
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<User>> getFriendList() {
        if (friendList.getValue().isEmpty())
            apiGetFriends();
        return friendList;
    }

    public void apiGetFriends(){
        //TODO get from api
        User user1 = new User(5,"Markus", "123456");
        User user2 = new User(5,"Anne", "123456");
        User user3 = new User(5,"Sonny", "123456");

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(user1);
        userArrayList.add(user2);
        userArrayList.add(user3);

        friendList.setValue(userArrayList);
    }

    public void apiAddUser(User user){
        //currentUser = new User(user.getUserId(), user.getUserName(), user.getPassword()); //Dummy Data
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
                            Log.e("Api error", call.toString());
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

    public void signOut() {
        currentUser = null;
    }

    public void apiSignIn(String userName, String password) {
        currentUser = new User(-1, userName, password); //Dummy Data
        /*
        User userToLogin = new User(-1, userName, password);
        //TODO Find ud af (snak med DAI?) om login skal ske med query-parameters (GET-request) eller via info i body på en måde
        //https://i.imgur.com/tdKpReE.png , https://i.imgur.com/7oFXQRE.png , https://i.imgur.com/2g5jEMN.png
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
            Call<User> call = growBroApi.login(userName, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        currentUser = response.body();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Api error", t.toString());
                }
            }); */
    }
}
