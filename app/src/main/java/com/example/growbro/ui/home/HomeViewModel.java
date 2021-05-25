package com.example.growbro.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HomeViewModel extends ViewModel {

    MutableLiveData<HashMap<Integer, Integer>> mNextMeasurementMinutes;
    private GreenhouseRepository repository;

    public HomeViewModel() {
        repository = GreenhouseRepository.getInstance();
        //repository.getDummyData(1);
        repository.apiGetCurrentData(3,1);
        mNextMeasurementMinutes = new MutableLiveData<>();
        mNextMeasurementMinutes.setValue(new HashMap<>());
    }

 /*   public void startTimerToNextMeasurement(int greenhouseId){
        //Task to be executed in timer
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    while(true){
                        int secondsBetweenIteration = 30;
                        Thread.sleep(secondsBetweenIteration * 1000);

                        //Calculate when next measurement will be
                        Date now = new Date();
                        Date nextMeasurement = new Date(repository.getGreenhouse(greenhouseId).getLastMeasurement().getTime() + (15 * 60 * 1000));
                        long diffInMillies = Math.abs(nextMeasurement.getTime() - now.getTime());
                        Log.d("timer", "Next reading in " + String.valueOf(TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)) + " minutes");
                        //TODO Skal finde ud af hvordan vi kan opdateret viewet fra seperat thread
                        minutesToNextMeasurement.postValue(String.valueOf(TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)) + " minutes");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //Creates new timer with the task and schedules it to start immediately
        //Timer timer = new Timer("Timer");
        //timer.schedule(task, 0);
    } */

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

    public void waterNow(int userid, int greenhouseid)
        {
            repository.apiWaterNow(userid,greenhouseid);
        }

    public LiveData<List<Greenhouse>> getGreenhouseListAsLiveData() {
        return repository.getGreenhouseListAsLiveData();
    }

    public User getCurrentUser() {
        return UserRepository.getInstance().getCurrentUser();
    }
}