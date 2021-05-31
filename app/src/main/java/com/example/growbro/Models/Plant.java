package com.example.growbro.Models;

import com.google.gson.annotations.SerializedName;

public class Plant {
    @SerializedName(value = "Name")
    private String name;
    private int plantId;
    private int greenHouseID;

    @SerializedName(value = "Url")
    private String imageUrl;

    public Plant(String name, int plantId, String imageUrl) {
        this.name = name;
        this.plantId = plantId;
        this.imageUrl = imageUrl;
    }

    public int getGreenHouseID() {
        return greenHouseID;
    }

    public void setGreenHouseID(int greenHouseID) {
        this.greenHouseID = greenHouseID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
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
