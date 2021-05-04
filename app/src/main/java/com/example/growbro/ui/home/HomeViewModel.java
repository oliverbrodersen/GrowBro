package com.example.growbro.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> minutesToNextMeasurement;
    private GreenhouseRepository repository;

    public HomeViewModel() {
        repository = GreenhouseRepository.getInstance();
        repository.getDummyData(1);
        minutesToNextMeasurement = new MutableLiveData<>();
        minutesToNextMeasurement.setValue("15 minutes");
    }
    public void startTimerToNextMeasurement(int greenhouseId){
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
                        //minutesToNextMeasurement.setValue(String.valueOf(TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)) + " minutes");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //Creates new timer with the task and schedules it to start immediately
        Timer timer = new Timer("Timer");
        timer.schedule(task, 0);
    }

    public MutableLiveData<String> getMinutesToNextMeasurement() {
        return minutesToNextMeasurement;
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
}