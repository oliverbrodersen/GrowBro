package com.example.growbro.Data;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreenhouseRepository {
    private static GreenhouseRepository instance;
    private GreenhouseDAO greenhouseDAO;

    private GreenhouseRepository(){
        greenhouseDAO = GreenhouseDAO.getInstance();
    }

    public static GreenhouseRepository getInstance() {
        if (instance == null)
            instance = new GreenhouseRepository();
        return instance;
    }
    public List<Greenhouse> getGreenhouseList(){
        return greenhouseDAO.getGreenhouseList();
    }
    public void updateGreenhouse(Greenhouse greenhouse){
        greenhouseDAO.updateGreenhouse(greenhouse);
    }
    public Greenhouse getGreenhouse(int id){
        return greenhouseDAO.getGreenhouse(id);
    }
    public void apiGetCurrentData(int userId, int greenhouseId){
        greenhouseDAO.apiGetCurrentData(userId, greenhouseId);
    }

    public MutableLiveData<List<SensorData>> getLiveData(int greenhouseId){
        return greenhouseDAO.getLiveData(greenhouseId);
    }
    public void apiWaterNow(int userId, int greenhouseId){
        greenhouseDAO.apiWaterNow(userId, greenhouseId);
    }
    public void apiLogin(String username, String password){
        greenhouseDAO.apiLogin(username, password);
    }
    public void apiGetGreenhouseList(int userId){
        greenhouseDAO.apiGetGreenhouseList(userId);
    }
    public void apiGetGreenhouse(int userId, int greenhouseId){
        greenhouseDAO.apiGetGreenhouse(userId, greenhouseId);
    }
    public void getDummyData(int greenhouseId){
        greenhouseDAO.getDummyData(greenhouseId);
    }
    public void apiAddPlant(int userId, int greenhouseId, Plant plant){
        greenhouseDAO.apiAddPlant(userId, greenhouseId, plant);
    }

    public void sendText(String value) {
//TODO: make it
    }

    public MutableLiveData<List<Greenhouse>> getGreenhouseListAsLiveData() {
        return greenhouseDAO.getGreenhouseListAsLiveData();
    }

    public ArrayList<Entry> getChartEntries(String parameterName, String selectedGreenhouseId) {

        HashMap<Long, Float> sensorDataList = greenhouseDAO.getSensorDataHistory(parameterName, selectedGreenhouseId);

        ArrayList<Entry> chartEntries = new ArrayList<>();

        float x;
        float y;

        for (Map.Entry<Long, Float> entry : sensorDataList.entrySet()) {
            x = entry.getKey();
            y = entry.getValue();
            if (y != 0f) {
                chartEntries.add(new Entry(x, y));
            }
        }
        return chartEntries;
    }
}
