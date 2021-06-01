package com.example.growbro.Data;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Login.UserRepository;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.github.mikephil.charting.data.Entry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreenhouseRepository {
    private static GreenhouseRepository instance;
    private GreenhouseDAO greenhouseDAO;
    private UserRepository userRepository;

    private GreenhouseRepository(){
        greenhouseDAO = GreenhouseDAO.getInstance();
        userRepository = UserRepository.getInstance();
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
    public void openWindow(int userId, int greenhouseId, int openWindow)
        {
            greenhouseDAO.apiOpenWindow(userId, greenhouseId, openWindow);
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

    public MutableLiveData<List<Greenhouse>> getGreenhouseListAsLiveData() {
        return greenhouseDAO.getGreenhouseListAsLiveData();
    }

    public ArrayList<Entry> getChartEntries(int userId, String parameterName, String selectedGreenhouseId, Timestamp timeFrom, Timestamp timeTo) {

        HashMap<Long, Float> sensorDataList = greenhouseDAO.apiGetSensorDataHistory(userId, parameterName, selectedGreenhouseId, timeFrom, timeTo);

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

    public MutableLiveData<List<Greenhouse>> getFriendsGreenhouseList() {
        greenhouseDAO.apiGetFriendsGreenhouseList(userRepository.getCurrentUser().getValue().getId());
        return greenhouseDAO.getFriendsGreenhouseListAsLiveData();
    }

    public void addGreenhouse(Greenhouse g) {
        greenhouseDAO.apiAddGreenhouse(g.getOwnerId(), g);
    }
}
