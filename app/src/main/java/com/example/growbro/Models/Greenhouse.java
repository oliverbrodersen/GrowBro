package com.example.growbro.Models;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.SensorData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Greenhouse {
    private String Name;
    private int greenHouseID;
    private int userID;
    private ArrayList<Plant> listPlants;
    private int waterFrequency;
    private double waterVolume;
    private String waterTimeOfDay;
    private boolean windowIsOpen;
    private List<SensorData> sensorData;
    private MutableLiveData<List<SensorData>> sensorDataLive;

    private ArrayList<Float> temperatureThreshold;
    private ArrayList<Float> humidityThreshold;
    private ArrayList<Float> co2Threshold;

    private Timestamp lastMeasurement;
    private CountDownTimer countDownTimerNextMeasurement;
    private MutableLiveData<Integer> minutesToNextMeasurement;
    private boolean isTimeToRestartMeasurementTimer;

    private Timestamp lastWaterDate;
    private CountDownTimer countDownTimerNextWater;
    private MutableLiveData<Integer> minutesToNextWater;
    private boolean isTimeToRestartWaterTimer;

    private static final int MEASUREMENT_INTERVAL_IN_MINUTES = 15;
    private ArrayList<String> sharedWith;

    public Greenhouse(){
        sensorDataLive = new MutableLiveData<>();
        listPlants = new ArrayList<>();
        windowIsOpen = false;
        lastMeasurement = new Timestamp(new Date().getTime());
        lastWaterDate = new Timestamp(new Date().getTime());
        minutesToNextMeasurement = new MutableLiveData<>();
        isTimeToRestartMeasurementTimer = false;
        minutesToNextWater = new MutableLiveData<>();
        sharedWith = new ArrayList<>();
        temperatureThreshold = new ArrayList<>();
        humidityThreshold = new ArrayList<>();
        co2Threshold = new ArrayList<>();

        temperatureThreshold.add(new Float(0));
        temperatureThreshold.add(new Float(30));
        humidityThreshold.add(new Float(15));
        humidityThreshold.add(new Float(80));
        co2Threshold.add(new Float(200));
        co2Threshold.add(new Float(1200));

        isTimeToRestartWaterTimer = false;
    }
    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, Timestamp lastMeasurement, List<SensorData> currentData, boolean windowIsOpen) {
        this.Name = name;
        greenHouseID = id;
        userID = ownerId;
        this.listPlants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        sensorDataLive = new MutableLiveData<>();
        sensorDataLive.setValue(currentData);
        this.windowIsOpen = windowIsOpen;

        this.lastWaterDate = lastWaterDate;
        minutesToNextWater = new MutableLiveData<>();
        isTimeToRestartWaterTimer = false;

        this.lastMeasurement = lastMeasurement;
        minutesToNextMeasurement = new MutableLiveData<>();
        isTimeToRestartMeasurementTimer = false;

        temperatureThreshold = new ArrayList<>();
        humidityThreshold = new ArrayList<>();
        co2Threshold = new ArrayList<>();

        sharedWith = new ArrayList<>();
        sharedWith.add("Bobber");
        sharedWith.add("Bob");
        sharedWith.add("Bopper");

        temperatureThreshold.add(new Float(0));
        temperatureThreshold.add(new Float(30));
        humidityThreshold.add(new Float(15));
        humidityThreshold.add(new Float(80));
        co2Threshold.add(new Float(200));
        co2Threshold.add(new Float(1200));
    }

    public List<SensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<SensorData> sensorData) {
        this.sensorData = sensorData;
        for (SensorData sd : sensorData){
            Log.i("Sensordata", sd.getType());
        }
        sensorDataLive.setValue(sensorData);
    }

    public boolean isWindowIsOpen() {
        return windowIsOpen;
    }

    public void setWindowIsOpen(boolean windowIsOpen) {
        this.windowIsOpen = windowIsOpen;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return greenHouseID;
    }

    public Timestamp getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurementAndResetLiveData(Timestamp lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
        reSetLiveDataMinutesToNextMeasurement();
    }

    public void setCurrentData(MutableLiveData<List<SensorData>> currentData) {
        sensorDataLive = currentData;
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
        return userID;
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
        for (SensorData d: sensorDataLive.getValue()) {
            if (d.getType().equals("CO2"))
                return d;
        }
        return null;
    }
    public SensorData getCurrentDataHumidity(){

        for (SensorData d: sensorDataLive.getValue()) {
            if (d.getType().equals("Humidity"))
                return d;
        }
        return null;
    }
    public SensorData getCurrentDataTemperature(){

        for (SensorData d: sensorDataLive.getValue()) {
            if (d.getType().equals("Temperature"))
                return d;
        }
        return null;
    }

    public List<SensorData> getCurrentData() {
        return sensorDataLive.getValue();
    }

    public MutableLiveData<List<SensorData>> getCurentLiveData(){
        return sensorDataLive;
    }

    public void setCurrentData(List<SensorData> currentData) {
        sensorDataLive.setValue(currentData);
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setId(int id) {
        greenHouseID = id;
    }

    public void setOwnerId(int ownerId) {
        userID = ownerId;
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

    public void setLastWaterDateAndResetLiveData(Timestamp lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
        reSetLiveDataMinutesToNextWater();
    }

    public LiveData<Integer> getMinutesToNextMeasurementLiveData() {
        return minutesToNextMeasurement;
    }

    public void reSetLiveDataMinutesToNextMeasurement() {
        minutesToNextMeasurement.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastMeasurement));
    }

    public void rePostLiveDataMinutesToNextMeasurement() {
        minutesToNextMeasurement.postValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastMeasurement));
    }

    public LiveData<Integer> getMinutesToNextWaterLiveData() {
        return minutesToNextWater;
    }

    public void reSetLiveDataMinutesToNextWater() {
        minutesToNextWater.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDate));
    }

    public void rePostLiveDataMinutesToNextWater() {
        minutesToNextWater.postValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDate));
    }


    public void addPlant(Plant plant){
        listPlants.add(plant);
    }

    public void setMinutesToNextMeasurement() {
        minutesToNextMeasurement.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDate));
    }

    public static long getMinutesSince(Timestamp timestamp)
    {
        long before;

        if (timestamp == null)
            before = new Timestamp(System.currentTimeMillis()).getTime();
        else
            before = timestamp.getTime();

        long now = new Timestamp(System.currentTimeMillis()).getTime();

        return (now - before) / (1000 * 60);
    }



public void startCountDownTimerNextMeasurement(){
    reSetLiveDataMinutesToNextMeasurement();
    long millisInFuture = getMinutesToNextMeasurementLiveData().getValue();

    millisInFuture = millisInFuture*60*1000; //Countdown kan testes hurtigere ved at skrive 100 i stedet for 1000 (så countdown hurtigere er forbi)

    if(countDownTimerNextMeasurement != null)
        countDownTimerNextMeasurement.cancel();

    countDownTimerNextMeasurement = new CountDownTimer(millisInFuture, 1000) {
        long remainingTime;

        public void onTick(long millisUntilFinished) {
            remainingTime = millisUntilFinished;
            long seconds = remainingTime / 1000;
            long minutes = 1 + seconds / 60;

            minutesToNextMeasurement.postValue((int) minutes);   //Countdown kan testes hurtigere ved at bruge seconds i stedet for minutes (så værdien opdateres ved hvert countdown-interval)
        }

        public void onFinish() {
            setIsTimeToRestartMeasurementTimer(true);
            this.cancel();
            if (lastMeasurement == null)
                lastMeasurement = new Timestamp(System.currentTimeMillis());
            //lastMeasurement.setTime(System.currentTimeMillis()); //Dummy data version --> TODO: GreenhouseDAO has to call this onResponse from api
            //rePostLiveDataMinutesToNextMeasurement(); //Dummy data version --> TODO: GreenhouseDAO has to call this onResponse from api
        }
    }.start();
}

    public void startCountDownTimerNextWater(){
        reSetLiveDataMinutesToNextWater();
        long millisInFuture;
        if (getMinutesToNextWaterLiveData() == null)
            millisInFuture = MEASUREMENT_INTERVAL_IN_MINUTES * 60 * 1000;
        else
            millisInFuture = getMinutesToNextWaterLiveData().getValue();

        millisInFuture = millisInFuture*60*1000; //Countdown kan testes hurtigere ved at skrive 100 i stedet for 1000 (så countdown hurtigere er forbi)

        if(countDownTimerNextWater != null)
            countDownTimerNextWater.cancel();

        countDownTimerNextWater = new CountDownTimer(millisInFuture, 1000) {
            long remainingTime;

            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                long seconds = remainingTime / 1000;
                long minutes = 1 + seconds / 60;

                minutesToNextWater.postValue((int) minutes);   //Countdown kan testes hurtigere ved at bruge seconds i stedet for minutes (så værdien opdateres ved hvert countdown-interval)
            }

            public void onFinish() {
                setIsTimeToRestartWaterTimer(true);
                this.cancel();

                if (lastWaterDate == null)
                    lastWaterDate = new Timestamp(System.currentTimeMillis());

                //lastWaterDate.setTime(System.currentTimeMillis()); //Dummy data version --> TODO: GreenhouseDAO has to call this on response from api
                //rePostLiveDataMinutesToNextWater(); //Dummy data version --> TODO: GreenhouseDAO has to call this on response from api
            }
        }.start();
    }

    public boolean isTimeToRestartMeasurementTimer() {
        return isTimeToRestartMeasurementTimer;
    }

    public void setIsTimeToRestartMeasurementTimer(boolean restartMeasurementTimer) {
        this.isTimeToRestartMeasurementTimer = restartMeasurementTimer;
    }

    public boolean isTimeToRestartWaterTimer() {
        return isTimeToRestartWaterTimer;
    }

    public void setIsTimeToRestartWaterTimer(boolean restartWaterTimer) {
        this.isTimeToRestartWaterTimer = restartWaterTimer;
    }

    public ArrayList<Float> getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(ArrayList<Float> temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }

    public ArrayList<Float> getHumidityThreshold() {
        return humidityThreshold;
    }

    public void setHumidityThreshold(ArrayList<Float> humidityThreshold) {
        this.humidityThreshold = humidityThreshold;
    }

    public ArrayList<Float> getCo2Threshold() {
        return co2Threshold;
    }

    public void setCo2Threshold(ArrayList<Float> co2Threshold) {
        this.co2Threshold = co2Threshold;
    }
}
