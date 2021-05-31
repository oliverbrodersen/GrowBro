package com.example.growbro.Models.Data;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GreenhouseUpload {




    private String Name;
    private int greenHouseID;
    private int userID;
    private ArrayList<Plant> Plants;
    private int waterFrequency;
    private boolean WindowIsOpen;
    private double waterVolume;
    private String waterTimeOfDay;
    private ArrayList<SensorData> sensorData;
    private String lastWaterDate;
    private String lastMeasurement;
    private ArrayList<String> sharedWith;
    @SerializedName(value = "tempteratureThreshhold")
    private List<Float> temperatureThreshold;
    private List<Float> humidityThreshold;
    @SerializedName(value = "co2Threshhold")
    private List<Float> co2Threshold;

    public GreenhouseUpload(Greenhouse greenhouse) {
        Plants = greenhouse.getPlants();
        greenHouseID = greenhouse.getId();
        WindowIsOpen = greenhouse.isWindowIsOpen();
        userID = greenhouse.getOwnerId();
        Name = greenhouse.getName();
        waterFrequency = greenhouse.getWaterFrequency();
        waterVolume = greenhouse.getWaterVolume();
        waterTimeOfDay = greenhouse.getWaterTimeOfDay();
        lastWaterDate = new Timestamp(new Date().getTime()).toString();
        lastMeasurement = new Timestamp(new Date().getTime()).toString();
        sensorData = (ArrayList<SensorData>) greenhouse.getSensorData();
        sharedWith = greenhouse.getSharedWith();
        temperatureThreshold = greenhouse.getTemperatureThreshold();
        humidityThreshold = greenhouse.getHumidityThreshold();
        co2Threshold = greenhouse.getCo2Threshold();
    }

    public boolean isWindowIsOpen() {
        return WindowIsOpen;
    }

    public List<Float> getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(List<Float> temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }

    public List<Float> getHumidityThreshold() {
        return humidityThreshold;
    }

    public void setHumidityThreshold(List<Float> humidityThreshold) {
        this.humidityThreshold = humidityThreshold;
    }

    public List<Float> getCo2Threshold() {
        return co2Threshold;
    }

    public void setCo2Threshold(List<Float> co2Threshold) {
        this.co2Threshold = co2Threshold;
    }

    public ArrayList getPlants() {
        return Plants;
    }

    public void setPlants(ArrayList plants) {
        Plants = plants;
    }

    public int getGreenHouseID() {
        return greenHouseID;
    }

    public void setGreenHouseID(int greenHouseID) {
        this.greenHouseID = greenHouseID;
    }

    public boolean getWindowIsOpen() {
        return WindowIsOpen;
    }

    public void setWindowIsOpen(boolean windowIsOpen) {
        WindowIsOpen = windowIsOpen;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getWaterFrequency() {
        return waterFrequency;
    }

    public void setWaterFrequency(int waterFrequency) {
        this.waterFrequency = waterFrequency;
    }

    public double getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(double waterVolume) {
        this.waterVolume = waterVolume;
    }

    public String getWaterTimeOfDay() {
        return waterTimeOfDay;
    }

    public void setWaterTimeOfDay(String waterTimeOfDay) {
        this.waterTimeOfDay = waterTimeOfDay;
    }

    public String getLastWaterDate() {
        return lastWaterDate;
    }

    public void setLastWaterDate(String lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
    }

    public String getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(String lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

    public ArrayList getSensorData() {
        return sensorData;
    }

    public void setSensorData(ArrayList sensorData) {
        this.sensorData = sensorData;
    }

    public ArrayList getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(ArrayList sharedWith) {
        this.sharedWith = sharedWith;
    }
}
