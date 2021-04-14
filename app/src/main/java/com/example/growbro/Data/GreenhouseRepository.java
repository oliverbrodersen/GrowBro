package com.example.growbro.Data;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;

import java.util.ArrayList;
import java.util.List;

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
    public void apiAddPlant(int userId, int greenhouseId, Plant plant){
        greenhouseDAO.apiAddPlant(userId, greenhouseId, plant);
    }
}
