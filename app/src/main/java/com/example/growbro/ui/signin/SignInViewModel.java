package com.example.growbro.ui.signin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.User;

public class SignInViewModel extends ViewModel {
    private UserRepository userRepository;

    public SignInViewModel() {
       userRepository = UserRepository.getInstance();
    }

    public MutableLiveData<User> getCurrentUser() {
        return UserRepository.getInstance().getCurrentUser();
    }

    public void signIn(String userName, String password) {
        userRepository.signIn(userName, password);
    }

    public void signOut() {
        userRepository.signOut();
    }

    public void addUser(String userName, String password) {
        userRepository.addUser(userName, password);
    }
}
