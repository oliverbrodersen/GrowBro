package com.example.growbro.Models.Data;

import java.sql.Timestamp;
import java.util.List;

public class ApiCurrentDataPackage {
    private List<Data> data;

    //Præcist tidspunkt for målingen på hardwaren
    private Timestamp lastDataPoint;

    public ApiCurrentDataPackage(List<Data> data, Timestamp lastDataPoint) {
        this.data = data;
        this.lastDataPoint = lastDataPoint;
    }

    public List<Data> getData() {
        return data;
    }

    public Timestamp getLastDataPoint() {
        return lastDataPoint;
    }
}

