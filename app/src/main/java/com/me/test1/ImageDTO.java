package com.me.test1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDTO {
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
