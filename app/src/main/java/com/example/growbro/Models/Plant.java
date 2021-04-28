package com.example.growbro.Models;

import java.util.ArrayList;

public class Plant {
    private String name;
    private int plantId;
    private int image;

    public Plant(String name, int plantId, int image) {
        this.name = name;
        this.plantId = plantId;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
