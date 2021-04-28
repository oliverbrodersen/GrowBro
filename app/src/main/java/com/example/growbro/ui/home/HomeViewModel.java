package com.example.growbro.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Models.Data.Data;
import com.example.growbro.Models.Greenhouse;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private GreenhouseRepository repository;

    public HomeViewModel() {
        repository = GreenhouseRepository.getInstance();
        repository.getDummyData(1,1);
        mText = new MutableLiveData<>();
        mText.setValue(repository.getGreenhouseList().get(0).getCurrentDataCo2().getData() + "");

    }

    public LiveData<String> getText() {
        return mText;
    }
    public List<Greenhouse> getGreenhouseList(){
        return repository.getGreenhouseList();
    }
    public void updateGreenhouse(Greenhouse greenhouse){
        repository.updateGreenhouse(greenhouse);
    }
    public Greenhouse getGreenhouse(int id){
        return repository.getGreenhouse(id);
    }
    public MutableLiveData<List<Data>> getLiveData(int greenhouseId){
        return repository.getLiveData(greenhouseId);
    }

    public void waterNow(int userid, int greenhouseid)
        {
            repository.apiWaterNow(userid,greenhouseid);
        }
}