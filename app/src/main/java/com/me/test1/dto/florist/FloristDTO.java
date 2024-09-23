package com.me.test1.dto.florist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloristDTO  extends BaseFloristDTO{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("plantsQuantity")
    @Expose
    private int plantsQuantity;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getPlantsQuantity() {
        return plantsQuantity;
    }
    public void setPlantsQuantity(int plantsQuantity) {
        this.plantsQuantity = plantsQuantity;
    }
}
