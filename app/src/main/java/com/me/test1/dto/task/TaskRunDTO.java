package com.me.test1.dto.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class TaskRunDTO {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("task")
    @Expose
    private TaskDTO task;
    @SerializedName("start")
    @Expose
    private java.time.LocalDateTime start;
    @SerializedName("completed")
    @Expose
    private LocalDateTime completed;
    @SerializedName("status")
    @Expose
    private TaskRunStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getCompleted() {
        return completed;
    }

    public void setCompleted(LocalDateTime completed) {
        this.completed = completed;
    }

    public TaskRunStatus getStatus() {
        return status;
    }

    public void setStatus(TaskRunStatus status) {
        this.status = status;
    }
}
