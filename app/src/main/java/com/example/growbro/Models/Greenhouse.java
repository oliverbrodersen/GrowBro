package com.example.growbro.Models;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.SensorData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Greenhouse {
    private String name;
    private int id;
    private int ownerId;
    private ArrayList<Plant> listPlants;
    private int waterFrequency;
    private double waterVolume;
    private String waterTimeOfDay;
    private Timestamp lastWaterDate;
    private Timestamp lastMeasurement;
    private boolean windowIsOpen;
    private MutableLiveData<List<SensorData>> currentData;
    private MutableLiveData<Integer> minutesToNextMeasurement;
    private CountDownTimer countDownTimer;
    private static final int MEASUREMENT_INTERVAL_IN_MINUTES = 15;
    private ArrayList<String> sharedWith;

    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, List<SensorData> currentData, boolean windowIsOpen) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.listPlants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        this.lastWaterDate = lastWaterDate;
        this.currentData = new MutableLiveData<>();
        this.currentData.setValue(currentData);
        this.windowIsOpen = windowIsOpen;
        minutesToNextMeasurement = new MutableLiveData<>();
        setMinutesToNextMeasurement();
        sharedWith = new ArrayList<>();
        sharedWith.add("Bobber");
        sharedWith.add("Bob");
        sharedWith.add("Bopper");
    }

    public boolean isWindowIsOpen() {
        return windowIsOpen;
    }

    public void setWindowIsOpen(boolean windowIsOpen) {
        this.windowIsOpen = windowIsOpen;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Timestamp getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(Timestamp lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

    public void setCurrentData(MutableLiveData<List<SensorData>> currentData) {
        this.currentData = currentData;
    }

    public ArrayList<String> getSharedWith() {
        return sharedWith;
    }
     public void shareGreenhouse(String username){
        sharedWith.add(username);
     }

     public void removeShare(String username){
        sharedWith.remove(username);
     }

    public int getOwnerId() {
        return ownerId;
    }

    public ArrayList<Plant> getListPlants() {
        return listPlants;
    }

    public int getWaterFrequency() {
        return waterFrequency;
    }

    public double getWaterVolume() {
        return waterVolume;
    }

    public String getWaterTimeOfDay() {
        return waterTimeOfDay;
    }

    public Timestamp getLastWaterDate() {
        return lastWaterDate;
    }

    public SensorData getCurrentDataCo2(){
        for (SensorData d:currentData.getValue()) {
            if (d.getType().equals("CO2"))
                return d;
        }
        return null;
    }
    public SensorData getCurrentDataHumidity(){

        for (SensorData d:currentData.getValue()) {
            if (d.getType().equals("Humidity"))
                return d;
        }
        return null;
    }
    public SensorData getCurrentDataTemperature(){

        for (SensorData d:currentData.getValue()) {
            if (d.getType().equals("Temperature"))
                return d;
        }
        return null;
    }
    public SensorData getCurrentDataLuminance(){

        for (SensorData d:currentData.getValue()) {
            if (d.getType().equals("CO2"))
                return d;
        }
        return null;
    }

    public List<SensorData> getCurrentData() {
        return currentData.getValue();
    }

    public MutableLiveData<List<SensorData>> getCurentLiveData(){
        return currentData;
    }

    public void setCurrentData(List<SensorData> currentData) {
        this.currentData.setValue(currentData);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setListPlants(ArrayList<Plant> listPlants) {
        this.listPlants = listPlants;
    }

    public void setWaterFrequency(int waterFrequency) {
        this.waterFrequency = waterFrequency;
    }

    public void setWaterVolume(double waterVolume) {
        this.waterVolume = waterVolume;
    }

    public void setWaterTimeOfDay(String waterTimeOfDay) {
        this.waterTimeOfDay = waterTimeOfDay;
    }

    public void setLastWaterDate(Timestamp lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
    }
    public void addPlant(Plant plant){
        listPlants.add(plant);
    }

    public LiveData<Integer> getMinutesToNextMeasurementLiveData() {
        return minutesToNextMeasurement;
    }

    public void setMinutesToNextMeasurement() {
        minutesToNextMeasurement.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDate));
    }

    public static long getMinutesSince(Timestamp timestamp)
    {
        long before = timestamp.getTime();
        long now = new Timestamp(System.currentTimeMillis()).getTime();

        return (now - before) / (1000 * 60);
    }

public void startCountDownTimer(){
    setMinutesToNextMeasurement();
    long millisInFuture = getMinutesToNextMeasurementLiveData().getValue();

    millisInFuture = millisInFuture*60*1000; //Countdown kan testes hurtigere ved at fjerne et nul (så countdown hurtigere er forbi)

    if(countDownTimer != null)
        countDownTimer.cancel();

    countDownTimer = new CountDownTimer(millisInFuture, 1000) {
        long remainingTime;

        public void onTick(long millisUntilFinished) {
            remainingTime = millisUntilFinished;
            long seconds = remainingTime / 1000;
            long minutes = 1 + seconds / 60;

            minutesToNextMeasurement.postValue((int) minutes);   //Countdown kan testes hurtigere ved at bruge seconds i stedet for minutes (så værdien opdateres ved hvert countdown-interval)
        }

        public void onFinish() {
            lastWaterDate.setTime(System.currentTimeMillis()); //TODO call API via GreenhouseRepository instead?
            this.cancel();
            startCountDownTimer();
        }
    }.start();
}
}
