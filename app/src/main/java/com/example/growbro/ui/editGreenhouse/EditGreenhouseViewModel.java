package com.example.growbro.ui.editGreenhouse;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.Greenhouse;

public class EditGreenhouseViewModel extends ViewModel {
    GreenhouseRepository greenhouseRepository;
    UserRepository userRepository;

    public EditGreenhouseViewModel() {
        greenhouseRepository = GreenhouseRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public void addGreenhouse(Greenhouse g) {
        //TODO update når login går gennem api
        //userRepository.getCurrentUser().getUserId();
        g.setOwnerId(1);
        greenhouseRepository.addGreenhouse(g);
    }

    public Greenhouse getGreenhouse(String selectedGreenhouseId) {
        return greenhouseRepository.getGreenhouse(Integer.parseInt(selectedGreenhouseId));
    }

    public void updateGreenhouse(Greenhouse greenhouse) {
        greenhouseRepository.updateGreenhouse(greenhouse);
    }
}