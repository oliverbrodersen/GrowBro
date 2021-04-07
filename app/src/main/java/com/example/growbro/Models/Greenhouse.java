package com.example.growbro.Models;

import com.example.growbro.Models.Data.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Greenhouse {
    private String name;
    private int id;
    private int ownerId;
    private ArrayList<Plant> listPlants;
    private int waterFrequency;
    private double waterVolume;
    private String waterTimeOfDay;
    private Timestamp lastWaterDate;
    private ArrayList<Data> currentData;

    public Greenhouse(String name, int id, int ownerId, ArrayList<Plant> listPlants, int waterFrequency, double waterVolume, String waterTimeOfDay, Timestamp lastWaterDate, ArrayList<Data> currentData) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.listPlants = listPlants;
        this.waterFrequency = waterFrequency;
        this.waterVolume = waterVolume;
        this.waterTimeOfDay = waterTimeOfDay;
        this.lastWaterDate = lastWaterDate;
        this.currentData = currentData;
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

    public ArrayList<Data> getCurrentData() {
        return currentData;
    }
}
