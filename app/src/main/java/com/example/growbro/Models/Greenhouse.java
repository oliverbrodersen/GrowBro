package com.example.growbro.Models;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.Data;
import com.example.growbro.Models.Data.DataType;

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
    private MutableLiveData<List<Data>> currentData;

    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, List<Data> currentData) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.listPlants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        this.lastWaterDate = lastWaterDate;
        this.currentData = new MutableLiveData<List<Data>>();
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

    public Data getCurrentDataCo2(){
        for (Data d:currentData.getValue()) {
            if (d.getType() == DataType.CO2)
                return d;
        }
        return null;
    }
    public Data getCurrentDataHumidity(){

        for (Data d:currentData.getValue()) {
            if (d.getType() == DataType.HUMIDITY)
                return d;
        }
        return null;
    }
    public Data getCurrentDataTemperature(){

        for (Data d:currentData.getValue()) {
            if (d.getType() == DataType.TEMPERATURE)
                return d;
        }
        return null;
    }
    public Data getCurrentDataLuminance(){

        for (Data d:currentData.getValue()) {
            if (d.getType() == DataType.LUMINANCE)
                return d;
        }
        return null;
    }

    public List<Data> getCurrentData() {
        return currentData.getValue();
    }

    public MutableLiveData<List<Data>> getCurentLiveData(){
        return currentData;
    }

    public void setCurrentData(List<Data> currentData) {
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
