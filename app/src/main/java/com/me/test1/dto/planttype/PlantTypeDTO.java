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

    @SerializedName("blossom")
    @Expose
    private String blossom;
    @SerializedName("sumWatering")
    @Expose
    private String sumWatering;
    @SerializedName("winWatering")
    @Expose
    private String winWatering;
    @SerializedName("light")
    @Expose
    private String light;
    @SerializedName("sumTempMin")
    @Expose
    private int sumTempMin;
    @SerializedName("sumTempMax")
    @Expose
    private int sumTempMax;
    @SerializedName("winTempMin")
    @Expose
    private int winTempMin;
    @SerializedName("winTempMax")
    @Expose
    private int winTempMax;
    @SerializedName("fertilize")
    @Expose
    private String fertilize;

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

    public String getBlossom() {
        return blossom;
    }

    public void setBlossom(String blossom) {
        this.blossom = blossom;
    }

    public String getSumWatering() {
        return sumWatering;
    }

    public void setSumWatering(String sumWatering) {
        this.sumWatering = sumWatering;
    }

    public String getWinWatering() {
        return winWatering;
    }

    public void setWinWatering(String winWatering) {
        this.winWatering = winWatering;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public int getSumTempMin() {
        return sumTempMin;
    }

    public void setSumTempMin(int sumTempMin) {
        this.sumTempMin = sumTempMin;
    }

    public int getSumTempMax() {
        return sumTempMax;
    }

    public void setSumTempMax(int sumTempMax) {
        this.sumTempMax = sumTempMax;
    }

    public int getWinTempMin() {
        return winTempMin;
    }

    public void setWinTempMin(int winTempMin) {
        this.winTempMin = winTempMin;
    }

    public int getWinTempMax() {
        return winTempMax;
    }

    public void setWinTempMax(int winTempMax) {
        this.winTempMax = winTempMax;
    }

    public String getFertilize() {
        return fertilize;
    }

    public void setFertilize(String fertilize) {
        this.fertilize = fertilize;
    }
}
