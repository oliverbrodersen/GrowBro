package com.example.growbro.Models.Data;

import java.util.ArrayList;

public class CurrentDataResultFromApi {
    ArrayList< SensorData > data = new ArrayList <SensorData>();
    private String lastDataPoint;

    public CurrentDataResultFromApi() {
    }

    public ArrayList<SensorData> getData() {
        return data;
    }

    public void setData(ArrayList<SensorData> data) {
        this.data = data;
    }

    public String getLastDataPoint() {
        return lastDataPoint;
    }

    public void setLastDataPoint(String lastDataPoint) {
        this.lastDataPoint = lastDataPoint;
    }
}
