package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModelOut {

    @SerializedName("name")
    @Expose
    private String Name;
    @SerializedName("email")
    @Expose
    private String Email;
    @SerializedName("password")
    @Expose
    private String Password;
    @SerializedName("phone")
    @Expose
    private int Phone;

    public UserModelOut() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }
}
