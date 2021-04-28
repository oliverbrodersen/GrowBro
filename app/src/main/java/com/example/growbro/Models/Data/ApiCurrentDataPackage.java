package com.example.growbro.Models.Data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

public class ApiCurrentDataPackage {
    @SerializedName("data")
    private List<SensorData> data;

    //Præcist tidspunkt for målingen på hardwaren
    private Timestamp lastDataPoint;

    public ApiCurrentDataPackage(){

    }

    public ApiCurrentDataPackage(List<SensorData> data, Timestamp lastDataPoint) {
        this.data = data;
        this.lastDataPoint = lastDataPoint;
    }

    public void setData(List<SensorData> data) {
        this.data = data;
    }

    public void setLastDataPoint(Timestamp lastDataPoint) {
        this.lastDataPoint = lastDataPoint;
    }

    public List<SensorData> getData() {
        return data;
    }

    public Timestamp getLastDataPoint() {
        return lastDataPoint;
    }
}

