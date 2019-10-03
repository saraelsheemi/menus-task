package com.vogella.android.myapplication.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TagItemDetailsResponse implements Serializable {

    @SerializedName("items")
    ArrayList<TagItemDetails> tagItemDetails;

    public ArrayList<TagItemDetails> getTagItemDetails() {
        return tagItemDetails;
    }

    public void setTagItemDetails(ArrayList<TagItemDetails> tagItemDetails) {
        this.tagItemDetails = tagItemDetails;
    }
}
