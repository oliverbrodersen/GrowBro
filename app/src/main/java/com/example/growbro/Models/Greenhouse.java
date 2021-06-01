package com.example.growbro.Models;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.R;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Greenhouse {
    private String Name;
    private int greenHouseID;
    private int userID;
    private ArrayList<Plant> Plants;
    private int waterFrequency;
    private boolean WindowIsOpen;
    private double waterVolume;
    private String waterTimeOfDay;
    private List<SensorData> sensorData;
    private MutableLiveData<List<SensorData>> sensorDataLive;
    private SimpleDateFormat sdf;

    @SerializedName(value = "tempteratureThreshhold")
    private List<Float> temperatureThreshold;
    private List<Float> humidityThreshold;
    @SerializedName(value = "co2Threshhold")
    private List<Float> co2Threshold;

    private String lastMeasurement;
    private Timestamp lastMeasurementTimestamp;
    private CountDownTimer countDownTimerNextMeasurement;
    private MutableLiveData<Integer> minutesToNextMeasurement;
    private boolean isTimeToRestartMeasurementTimer;

    private boolean stopCheckForNewMeasurement;

    private String lastWaterDate;
    private Timestamp lastWaterDateTimestamp;
    private CountDownTimer countDownTimerNextWater;
    private MutableLiveData<Integer> minutesToNextWater;
    private boolean isTimeToRestartWaterTimer;

    private static final int MEASUREMENT_INTERVAL_IN_MINUTES = 15;
    private ArrayList<String> sharedWith;

    public Greenhouse(){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WindowIsOpen = false;
        stopCheckForNewMeasurement = false;
        sensorDataLive = new MutableLiveData<>();
        Plants = new ArrayList<>();
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
    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, Timestamp lastMeasurement, List<SensorData> currentData, boolean WindowIsOpen) {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stopCheckForNewMeasurement = false;
        this.Name = name;
        greenHouseID = id;
        userID = ownerId;
        this.Plants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        sensorDataLive = new MutableLiveData<>();
        sensorDataLive.setValue(currentData);
        this.WindowIsOpen = WindowIsOpen;

        this.lastWaterDateTimestamp = lastWaterDate;
        minutesToNextWater = new MutableLiveData<>();
        isTimeToRestartWaterTimer = false;

        this.lastMeasurementTimestamp = lastMeasurement;
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

    public String getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(String lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

    public String getLastWaterDate() {
        return lastWaterDate;
    }

    public void setLastWaterDate(String lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
    }

    public boolean isWindowIsOpen() {
        return WindowIsOpen;
    }

    public void setWindowIsOpen(boolean windowIsOpen) {
        WindowIsOpen = windowIsOpen;
    }

    public boolean isStopCheckForNewMeasurement() {
        return stopCheckForNewMeasurement;
    }

    public void setStopCheckForNewMeasurement(boolean stopCheckForNewMeasurement) {
        this.stopCheckForNewMeasurement = stopCheckForNewMeasurement;
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

    public String getName() {
        return Name;
    }

    public int getId() {
        return greenHouseID;
    }

    public Timestamp getLastMeasurementTimestamp() {
        return lastMeasurementTimestamp;
    }

    public void setLastMeasurementAndResetLiveData(Timestamp lastMeasurement) {
        this.lastMeasurementTimestamp = lastMeasurement;
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

    public ArrayList<Plant> getPlants() {
        return Plants;
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

    public Timestamp getLastWaterDateTimestamp() {
        return lastWaterDateTimestamp;
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

    public void setPlants(ArrayList<Plant> plants) {
        this.Plants = plants;
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
        this.lastWaterDateTimestamp = lastWaterDate;
        reSetLiveDataMinutesToNextWater();
    }

    public LiveData<Integer> getMinutesToNextMeasurementLiveData() {
        return minutesToNextMeasurement;
    }

    public void reSetLiveDataMinutesToNextMeasurement() {
        if (lastMeasurementTimestamp == null)
            lastMeasurementTimestamp = Timestamp.valueOf(lastMeasurement);
        minutesToNextMeasurement.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastMeasurementTimestamp));
    }

    public void rePostLiveDataMinutesToNextMeasurement() {
        if (lastMeasurementTimestamp == null)
            lastMeasurementTimestamp = Timestamp.valueOf(lastMeasurement);
        minutesToNextMeasurement.postValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastMeasurementTimestamp));
    }

    public LiveData<Integer> getMinutesToNextWaterLiveData() {
        return minutesToNextWater;
    }

    public void reSetLiveDataMinutesToNextWater() {
        if (lastWaterDateTimestamp == null)
            lastWaterDateTimestamp = Timestamp.valueOf(lastWaterDate);
        minutesToNextWater.setValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDateTimestamp));
    }

    public void rePostLiveDataMinutesToNextWater() {
        if (lastWaterDateTimestamp == null)
            lastWaterDateTimestamp = Timestamp.valueOf(lastWaterDate);
        minutesToNextWater.postValue(MEASUREMENT_INTERVAL_IN_MINUTES - (int) getMinutesSince(lastWaterDateTimestamp));
    }


    public void addPlant(Plant plant){
        Plants.add(plant);
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
            if (lastMeasurementTimestamp == null)
                lastMeasurementTimestamp = new Timestamp(System.currentTimeMillis());
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

                if (lastWaterDateTimestamp == null)
                    lastWaterDateTimestamp = new Timestamp(System.currentTimeMillis());
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
        return (ArrayList<Float>) temperatureThreshold;
    }

    public void setTemperatureThreshold(ArrayList<Float> temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }

    public ArrayList<Float> getHumidityThreshold() {
        return (ArrayList<Float>) humidityThreshold;
    }

    public void setHumidityThreshold(ArrayList<Float> humidityThreshold) {
        this.humidityThreshold = humidityThreshold;
    }

    public ArrayList<Float> getCo2Threshold() {
        return (ArrayList<Float>) co2Threshold;
    }

    public void setCo2Threshold(ArrayList<Float> co2Threshold) {
        this.co2Threshold = co2Threshold;
    }

    public String getLastMeasurementToString() {
        return lastMeasurementTimestamp.toString();
        //Todo ensure that this is formatted the same way as timestamp from currentDataResultFromAPI
    }
    public int gethealthColor(String type){
        ArrayList<Float> thresholds;
        SensorData data;
        //Match data
        switch (type){
            case "temperature":
                thresholds = (ArrayList<Float>) temperatureThreshold;
                data = getCurrentDataTemperature();
                break;
            case "humidity":
                thresholds = (ArrayList<Float>) humidityThreshold;
                data = getCurrentDataHumidity();
                break;
            case "co2":
                thresholds = (ArrayList<Float>) co2Threshold;
                data = getCurrentDataCo2();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        //Check for range
        double buffer = (thresholds.get(1) - thresholds.get(0)) * 0.05;

        if (data.getValue() > (thresholds.get(0) - buffer) && data.getValue() < (thresholds.get(1) + buffer)) {
            if (data.getValue() > (thresholds.get(0) + buffer) && data.getValue() < (thresholds.get(1) - buffer))
                return R.color.goodHealth;

            return R.color.mediocreHealth;
        }
        return R.color.criticalHealth;
    }
}
