package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModelExternalOut {
    @SerializedName("email")
    @Expose
    public String Email;
    @SerializedName("name")
    @Expose
    public String Name;

    public LoginModelExternalOut() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
