package com.example.growbro.Models.Data;

import androidx.annotation.NonNull;

public class SensorData {
    private String type;
    private float data;

    public SensorData() {
    }

    public SensorData(String type, float data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "Type: " + type + ", Value: " + data;
    }
}
