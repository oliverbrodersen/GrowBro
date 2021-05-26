package com.example.growbro.ui.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.User;

import java.util.ArrayList;

public class FriendsViewModel extends ViewModel {
    GreenhouseRepository greenhouseRepository;
    UserRepository userRepository;

    public FriendsViewModel() {
        greenhouseRepository = GreenhouseRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public LiveData<ArrayList<User>> getFriendList() {
        return userRepository.getFriendList();
    }
    // TODO: Implement the ViewModel
}