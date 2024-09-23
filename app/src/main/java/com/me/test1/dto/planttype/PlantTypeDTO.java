package com.me.test1.dto.planttype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlantTypeDTO {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latinName")
    @Expose
    private String latinName;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatinName() {
        return latinName;
    }
    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }
}
