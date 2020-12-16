package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class DonationTagModelIn {
    @SerializedName("tagId")
    private TagModelIn Tag;

    public DonationTagModelIn() {
    }

    public TagModelIn getTag() {
        return Tag;
    }

    public void setTag(TagModelIn tag) {
        Tag = tag;
    }
}
