package com.me.test1.dto.plant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPlantRequestDTO extends BasePlantDTO{
    @SerializedName("plantTypeId")
    @Expose
    private Long plantTypeId;

    @SerializedName("floristId")
    @Expose
    private Long floristId;


    public Long getPlantTypeId() {return plantTypeId;}
    public void setPlantTypeId(Long plantTypeId) {this.plantTypeId = plantTypeId;}

    public Long getFloristId() {return floristId;}

    public void setFloristId(Long floristId) {this.floristId = floristId;}
}
