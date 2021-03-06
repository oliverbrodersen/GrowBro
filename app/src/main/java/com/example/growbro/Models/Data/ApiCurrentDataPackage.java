package com.example.growbro.Models.Data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApiCurrentDataPackage {
    @SerializedName("data")
    private List<SensorData> data;

    //Exact time for measurement from the Hardware
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

