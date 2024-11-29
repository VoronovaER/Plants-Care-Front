package com.me.test1.dto.florist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.me.test1.dto.task.TaskListRecordDTO;
import com.me.test1.dto.task.TaskRunDTO;

import java.time.LocalDateTime;

public class FloristTaskDTO {
    @SerializedName("runDateTime")
    @Expose
    LocalDateTime localDateTime;

    @SerializedName("task")
    @Expose
    TaskListRecordDTO task;
    @SerializedName("taskRun")
    @Expose
    TaskRunDTO taskRun;


    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    public TaskListRecordDTO getTask() {
        return task;
    }
    public void setTask(TaskListRecordDTO task) {
        this.task = task;
    }

    public TaskRunDTO getTaskRun() {
        return taskRun;
    }

    public void setTaskRun(TaskRunDTO taskRun) {
        this.taskRun = taskRun;
    }
}
