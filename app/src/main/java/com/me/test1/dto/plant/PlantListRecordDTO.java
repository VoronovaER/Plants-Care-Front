package com.me.test1.dto.plant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlantListRecordDTO extends BasePlantDTO{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("plantType")
    @Expose
    private String plantType;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPlantType() {
        return plantType;
    }
    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }
}