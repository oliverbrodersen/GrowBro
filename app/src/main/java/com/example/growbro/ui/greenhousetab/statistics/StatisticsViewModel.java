package com.example.growbro.ui.greenhousetab.statistics;

import androidx.lifecycle.ViewModel;

import com.example.growbro.Data.GreenhouseRepository;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsViewModel extends ViewModel {
    GreenhouseRepository greenhouseRepository = GreenhouseRepository.getInstance();

    public ArrayList<Entry> getChartEntries(String parameterName, String selectedGreenhouseId) {

        ArrayList<Entry> chartEntries = greenhouseRepository.getChartEntries(parameterName, selectedGreenhouseId);

        Collections.sort(chartEntries, (entry1, entry2) -> Float.compare(entry1.getX(), entry2.getX()));

        return chartEntries;
    }

    public String[] getParameterStrings(String selectedGreenhouseId) {
        String[] parameterNames = {"Temperature", "CO2", "Humidity"};
        return parameterNames;
    }
}