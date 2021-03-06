package com.example.growbro.ui.greenhousetab.greenhouse;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.Greenhouse;

public class GreenhouseViewModel extends ViewModel {
    private GreenhouseRepository greenhouseRepository;
    private UserRepository userRepository;

    public GreenhouseViewModel() {
        greenhouseRepository = GreenhouseRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public Greenhouse getGreenhouseFromId(int selectedGreenhouseId) {
        return greenhouseRepository.getGreenhouse(selectedGreenhouseId);
    }

    public int getCurrentUserId(){
        return userRepository.getCurrentUser().getValue().getId();
    }

    public void openWindow(int userId, int greenhouseId, int openWindow)
        {
            greenhouseRepository.openWindow(userId,greenhouseId, openWindow);
        }

    public void water(int userId, int greenhouseId)
        {
            greenhouseRepository.apiWaterNow(userId,greenhouseId);
        }
}