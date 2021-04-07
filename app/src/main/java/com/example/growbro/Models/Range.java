package com.example.growbro.Models;

import com.example.growbro.Models.Data.DataType;

public class Range {
    private double min;
    private double max;
    private DataType dataType;

    public Range(double min, double max, DataType dataType) {
        this.min = min;
        this.max = max;
        this.dataType = dataType;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public DataType getDataType() {
        return dataType;
    }
}
