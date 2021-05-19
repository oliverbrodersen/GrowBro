package com.example.growbro.ui.signin;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.User;

public class SignInViewModel extends ViewModel {
    private UserRepository userRepository;

    public SignInViewModel() {
       userRepository = UserRepository.getInstance();
    }

    public void login(String userName, String password) {
            userRepository.login(userName, password);
    }

    public User getCurrentUser() {
        return UserRepository.getInstance().getCurrentUser();
    }
}
