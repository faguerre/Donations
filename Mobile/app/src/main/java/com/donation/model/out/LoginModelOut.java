package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModelOut {
    @SerializedName("email")
    @Expose
    public String Email;
    @SerializedName("password")
    @Expose
    public String Password;

    public LoginModelOut() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
