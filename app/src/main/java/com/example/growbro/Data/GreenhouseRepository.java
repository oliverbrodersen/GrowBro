package com.example.growbro.Data;

import com.example.growbro.Models.Greenhouse;

import java.util.ArrayList;

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
    public ArrayList<Greenhouse> getGreenhouseList(){
        return greenhouseDAO.getGreenhouseList();
    }
    public void updateGreenhouse(Greenhouse greenhouse){
        greenhouseDAO.updateGreenhouse(greenhouse);
    }
    public Greenhouse getGreenhouse(int id){
        return greenhouseDAO.getGreenhouse(id);
    }
}
