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
    // TODO: Implement the ViewModel
}