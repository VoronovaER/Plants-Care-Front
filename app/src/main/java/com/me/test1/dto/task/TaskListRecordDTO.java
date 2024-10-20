package com.me.test1.dto.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskListRecordDTO extends BaseTaskDTO{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("plantTypeName")
    @Expose
    private String plantTypeName;
    @SerializedName("plantName")
    @Expose
    private String plantName;
    @SerializedName("period")
    @Expose
    private TaskPeriod period;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPlantTypeName() {
        return plantTypeName;
    }

    public void setPlantTypeName(String plantTypeName) {
        this.plantTypeName = plantTypeName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public TaskPeriod getPeriod() {
        return period;
    }
}
