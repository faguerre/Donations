package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class TagModelIn {
    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;

    public TagModelIn() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
