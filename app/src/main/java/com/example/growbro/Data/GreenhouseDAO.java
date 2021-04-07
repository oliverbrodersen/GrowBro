package com.example.growbro.Data;

import com.example.growbro.Models.Greenhouse;

import java.util.ArrayList;

public class GreenhouseDAO {
    private static GreenhouseDAO instance;
    private ArrayList<Greenhouse> greenhouseList;

    private GreenhouseDAO() {

    }

    public static GreenhouseDAO getInstance() {
        if (instance == null)
            instance = new GreenhouseDAO();
        return instance;
    }

    public ArrayList<Greenhouse> getGreenhouseList() {
        return greenhouseList;
    }
    public Greenhouse getGreenhouse(int id){
        for (Greenhouse gh:greenhouseList) {
            if (gh.getId() == id)
                return gh;
        }
        return null;
    }

    public void updateGreenhouse(Greenhouse greenhouse){
        for (int i = 0; i < greenhouseList.size(); i++) {
            if (greenhouseList.get(i).getId() == greenhouse.getId()){
                greenhouseList.remove(i);
                greenhouseList.add(greenhouse);
                break;
            }
        }
        System.out.println("Greenhouse was not found in DAO");
    }

    public void setGreenhouseList(ArrayList<Greenhouse> greenhouseList) {
        this.greenhouseList = greenhouseList;
    }
    public void addGreenhouse(Greenhouse greenhouse){
        greenhouseList.add(greenhouse);
    }
}
