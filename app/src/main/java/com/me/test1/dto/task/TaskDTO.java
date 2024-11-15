package com.me.test1.dto.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.me.test1.dto.plant.PlantDTO;

import java.time.LocalDateTime;

public class TaskDTO extends BaseTaskDTO{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("plant")
    @Expose
    private PlantDTO plantDTO;
    @SerializedName("startDate")
    @Expose
    private LocalDateTime startDate;
    @SerializedName("endDate")
    @Expose
    private LocalDateTime endDate;
    @SerializedName("period")
    @Expose
    private TaskPeriod period;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlantDTO getPlantDTO() {
        return plantDTO;
    }

    public void setPlantDTO(PlantDTO plantDTO) {
        this.plantDTO = plantDTO;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public TaskPeriod getPeriod() {
        return period;
    }

    public void setPeriod(TaskPeriod period) {
        this.period = period;
    }
}
