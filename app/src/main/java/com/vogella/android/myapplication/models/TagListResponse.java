package com.vogella.android.myapplication.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TagListResponse implements Serializable {

    @SerializedName("tags")
    ArrayList<TagItem> tagItems;

    public ArrayList<TagItem> getTagItems() {
        return tagItems;
    }

    public void setTagItems(ArrayList<TagItem> tagItems) {
        this.tagItems = tagItems;
    }
}
