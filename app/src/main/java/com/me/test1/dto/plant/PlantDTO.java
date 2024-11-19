package com.me.test1.dto.plant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.planttype.PlantTypeDTO;

import java.util.ArrayList;

public class PlantDTO extends BasePlantDTO{
    @SerializedName("plantType")
    @Expose
    private PlantTypeDTO plantType;
    @SerializedName("florists")
    @Expose
    private ArrayList<FloristDTO> florists;
    @SerializedName("id")
    @Expose
    private Long id;

    public PlantTypeDTO getPlantType() {return plantType;}
    public void setPlantType(PlantTypeDTO plantType) {this.plantType = plantType;}

    public ArrayList<FloristDTO> getFlorists() {return florists;}
    void setFlorists(ArrayList<FloristDTO> florists) {this.florists = florists;}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public PlantListRecordDTO getPlantListRecordDTO(){
        PlantListRecordDTO plantListRecordDTO = new PlantListRecordDTO();
        plantListRecordDTO.setId(id);
        plantListRecordDTO.setPlantType(plantType.getName());
        return plantListRecordDTO;
    }

    public BasePlantDTO getBasePlantDTO(){
        BasePlantDTO basePlantDTO = new BasePlantDTO();
        basePlantDTO.setName(this.getName());
        basePlantDTO.setPlace(this.getPlace());
        basePlantDTO.setUrl(this.getUrl());
        basePlantDTO.setDescription(this.getDescription());
        return basePlantDTO;
    }
}
