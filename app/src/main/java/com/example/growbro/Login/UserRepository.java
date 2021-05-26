package com.example.growbro.Login;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.User;

import java.util.ArrayList;

public class UserRepository {
    private static UserRepository instance;
    private UserDAO userDAO;

    private UserRepository() {
        userDAO = UserDAO.getInstance();
    }

    public static UserRepository getInstance() {
        if(instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    public MutableLiveData<ArrayList<User>> getFriendList(){
        return userDAO.getFriendList();
    }
    public void apiGetFriends(){
        userDAO.apiGetFriends();
    }
    public User getCurrentUser() {
        return userDAO.getCurrentUser();
    }

    public void signIn(String userName, String password) {
        userDAO.apiSignIn(userName, password);
    }

    public void signOut() {
        userDAO.signOut();
    }

    public void addUser(String userName, String password) {
        User user = new User(-1, userName, password); //-1 for userid here, because real userid will be set by database
        userDAO.apiAddUser(user);
    }
}
