package com.vogella.android.myapplication.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TagItemDetails implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("description")
    private String description;

    public TagItemDetails() {
    }

    public TagItemDetails(int id, String name, String photoUrl, String description) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
