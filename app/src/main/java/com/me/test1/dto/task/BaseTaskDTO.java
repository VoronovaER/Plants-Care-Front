package com.me.test1.dto.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class BaseTaskDTO {
    @SerializedName("createdAt")
    @Expose
    private LocalDateTime createdAt;

    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    @SerializedName("type")
    @Expose
    private TaskType type;

    @SerializedName("name")
    @Expose
    private String name;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
