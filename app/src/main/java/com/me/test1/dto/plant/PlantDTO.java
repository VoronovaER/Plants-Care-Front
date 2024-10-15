package com.me.test1.dto.plant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.me.test1.dto.planttype.PlantTypeDTO;

public class PlantDTO extends BasePlantDTO{
    @SerializedName("plantType")
    @Expose
    private PlantTypeDTO plantType;
    @SerializedName("floristId")
    @Expose
    private Long floristId;
    @SerializedName("id")
    @Expose
    private Long id;


    public PlantTypeDTO getPlantType() {return plantType;}
    public void setPlantType(PlantTypeDTO plantType) {this.plantType = plantType;}

    public Long getFloristId() {return floristId;}

    public void setFloristId(Long floristId) {this.floristId = floristId;}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
