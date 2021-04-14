package com.example.growbro.Models.Data;

import java.sql.Timestamp;

public class ApiReceipt {
    private Timestamp timeOfExecution;

    public ApiReceipt(Timestamp timeOfExecution) {
        this.timeOfExecution = timeOfExecution;
    }

    public Timestamp getTimeOfExecution() {
        return timeOfExecution;
    }
}
