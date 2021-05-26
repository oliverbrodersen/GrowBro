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
        //TODO update method so that it is not static and returns from userRepository.getCurrentUser().getUserId()
        return 1;
    }

    public void openWindow(int userId, int greenhouseId)
        {
            repository.openWindow(userId,greenhouseId);
        }

    public void water(int userId, int greenhouseId)
        {
            repository.apiWaterNow(userId,greenhouseId);
        }



    // TODO: Implement the ViewModel
}