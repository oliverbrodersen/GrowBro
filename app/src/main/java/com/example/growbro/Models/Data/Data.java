package com.example.growbro.Models.Data;

public abstract class Data {
    private double data;
    private DataType type;

    public Data(double data, DataType type) {
        this.data = data;
        this.type = type;
    }

    public double getData() {
        return data;
    }

    public DataType getType() {
        return type;
    }
}
