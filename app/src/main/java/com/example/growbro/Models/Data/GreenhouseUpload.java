package com.example.growbro.Models.Data;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;

import java.util.ArrayList;

public class GreenhouseUpload {
    private ArrayList<Plant> Plants;
    private int greenHouseID;
    private int WindowIsOpen;
    private int userID;
    private String Name;
    private int waterFrequency;
    private double waterVolume;
    private String waterTimeOfDay;
    private String lastWaterDate;
    private String lastMeasurement;
    private ArrayList<SensorData> sensorData;
    private ArrayList<String> sharedWith;

    public GreenhouseUpload(Greenhouse greenhouse) {
        Plants = greenhouse.getListPlants();
        greenHouseID = greenhouse.getId();
        WindowIsOpen = greenhouse.isWindowIsOpen();
        userID = greenhouse.getOwnerId();
        Name = greenhouse.getName();
        waterFrequency = greenhouse.getWaterFrequency();
        waterVolume = greenhouse.getWaterVolume();
        waterTimeOfDay = greenhouse.getWaterTimeOfDay();
        lastWaterDate = greenhouse.getLastWaterDate().toString();
        lastMeasurement = greenhouse.getLastMeasurement().toString();
        sensorData = (ArrayList<SensorData>) greenhouse.getSensorData();
        sharedWith = greenhouse.getSharedWith();
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

    public int getWindowIsOpen() {
        return WindowIsOpen;
    }

    public void setWindowIsOpen(int windowIsOpen) {
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
