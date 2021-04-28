package com.example.growbro.Models;

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
    private MutableLiveData<List<SensorData>> currentData;

    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, List<SensorData> currentData) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.listPlants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        this.lastWaterDate = lastWaterDate;
        this.currentData = new MutableLiveData<List<SensorData>>();
        this.currentData.setValue(currentData);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
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
}
