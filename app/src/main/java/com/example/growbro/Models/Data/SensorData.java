package com.example.growbro.Models.Data;

public class SensorData {
    private String type;
    private double value;

    public SensorData() {
    }

    public SensorData(String type, double value) {
        this.type = type;
        this.value = value;
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

    public void setValue(double value) {
        this.value = value;
    }
}
