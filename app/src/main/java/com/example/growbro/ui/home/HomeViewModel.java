package com.example.growbro.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.User;

import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    MutableLiveData<HashMap<Integer, Integer>> mNextMeasurementMinutes;
    MutableLiveData<HashMap<Integer, Integer>> mNextWaterMinutes;
    private GreenhouseRepository repository;
    private UserRepository userRepository;

    public HomeViewModel() {
        repository = GreenhouseRepository.getInstance();
        userRepository = UserRepository.getInstance();
        //repository.getDummyData(1);
        //repository.apiGetCurrentData(3,1);
        mNextMeasurementMinutes = new MutableLiveData<>();
        mNextMeasurementMinutes.setValue(new HashMap<>());
        mNextWaterMinutes = new MutableLiveData<>();
        mNextWaterMinutes.setValue(new HashMap<>());
    }

    public MutableLiveData<HashMap<Integer, Integer>> getMinutesToNextMeasurement() {

        if(mNextMeasurementMinutes.getValue().isEmpty()){
            HashMap<Integer, Integer> nextMeasurementMinutes = new HashMap<>();
            for (Greenhouse greenhouse : getGreenhouseList()) {
                nextMeasurementMinutes.put(greenhouse.getId(), greenhouse.getMinutesToNextMeasurementLiveData().getValue());
            }
            mNextMeasurementMinutes.setValue(nextMeasurementMinutes);
        }

        return mNextMeasurementMinutes;
    }

    public MutableLiveData<HashMap<Integer, Integer>> getMinutesToNextWater() {

        if(mNextWaterMinutes.getValue().isEmpty()){
            HashMap<Integer, Integer> nextWaterMinutes = new HashMap<>();
            for (Greenhouse greenhouse : getGreenhouseList()) {
                nextWaterMinutes.put(greenhouse.getId(), greenhouse.getMinutesToNextWaterLiveData().getValue());
            }
            mNextWaterMinutes.setValue(nextWaterMinutes);
        }

        return mNextWaterMinutes;
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
    public MutableLiveData<List<SensorData>> getLiveData(int greenhouseId){
        return repository.getLiveData(greenhouseId);
    }

    public LiveData<List<Greenhouse>> getGreenhouseListAsLiveData() {
        return repository.getGreenhouseListAsLiveData();
    }

    public MutableLiveData<User> getCurrentUser() {
        return UserRepository.getInstance().getCurrentUser();
    }

    public MutableLiveData<List<Greenhouse>> getFriendsGreenhouseList() {
        return repository.getFriendsGreenhouseList();
    }

    public void getGreenhouseListFromApi() {
        if (userRepository.getCurrentUser().getValue() != null)
            repository.apiGetGreenhouseList(userRepository.getCurrentUser().getValue().getId());
    }
}