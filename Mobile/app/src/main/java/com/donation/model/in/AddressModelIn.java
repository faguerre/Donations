package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class AddressModelIn {

    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("latitute")
    private String latitute;
    @SerializedName("longitute")
    private String longitute;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }
}
