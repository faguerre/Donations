package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModelOut {

    @SerializedName("name")
    @Expose
    private String Name;
    @SerializedName("latitute")
    @Expose
    private String Latitute;
    @SerializedName("longitute")
    @Expose
    private String Longitute;

    public AddressModelOut() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatitute() {
        return Latitute;
    }

    public void setLatitute(String latitute) {
        Latitute = latitute;
    }

    public String getLongitute() {
        return Longitute;
    }

    public void setLongitute(String longitute) {
        Longitute = longitute;
    }
}
