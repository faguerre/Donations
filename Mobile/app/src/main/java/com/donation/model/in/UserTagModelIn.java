package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class UserTagModelIn {
    @SerializedName("tagId")
    private int TagId;

    public UserTagModelIn() {
    }

    public int getTag() {
        return TagId;
    }

    public void setTag(int tagId) {
        TagId = tagId;
    }
}
