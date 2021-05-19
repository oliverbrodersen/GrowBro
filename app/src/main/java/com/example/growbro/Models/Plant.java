package com.example.growbro.Models;

import java.util.ArrayList;

public class Plant {
    private String name;
    private int plantId;
    private String imageUrl;

    public Plant(String name, int plantId, String imageUrl) {
        this.name = name;
        this.plantId = plantId;
        this.imageUrl = imageUrl;
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

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
