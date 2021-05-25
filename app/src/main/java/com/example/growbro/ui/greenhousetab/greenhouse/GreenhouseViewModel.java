package com.example.growbro.ui.greenhousetab.greenhouse;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Models.Greenhouse;

public class GreenhouseViewModel extends ViewModel {
    private GreenhouseRepository repository;

    public GreenhouseViewModel() {
        repository = GreenhouseRepository.getInstance();
    }

    public Greenhouse getGreenhouseFromId(int selectedGreenhouseId) {
        return repository.getGreenhouse(selectedGreenhouseId);
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