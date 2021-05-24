package com.example.growbro.ui.greenhousetab.statistics;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.github.mikephil.charting.data.Entry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

public class StatisticsViewModel extends ViewModel {
    GreenhouseRepository greenhouseRepository = GreenhouseRepository.getInstance();

    public ArrayList<Entry> getChartEntries(int userId, String parameterName, String selectedGreenhouseId, Timestamp timeFrom, Timestamp timeTo) {

        ArrayList<Entry> chartEntries = greenhouseRepository.getChartEntries(userId, parameterName, selectedGreenhouseId, timeFrom, timeTo);

        chartEntries.sort((entry1, entry2) -> Float.compare(entry1.getX(), entry2.getX()));

        return chartEntries;
    }
}