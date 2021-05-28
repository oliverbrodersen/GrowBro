package com.example.growbro.Models.Data;

import androidx.annotation.NonNull;

public class SensorData {
    private String type;
    private int value;
    //TODO value hedder data når den kommer fra den rigtige API
    public SensorData() {
    }

    public SensorData(String type, int data) {
        this.type = type;
        this.value = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return "Type: " + type + ", Value: " + value;
    }
}
