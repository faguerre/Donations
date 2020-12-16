package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserModelIn {

    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("email")
    private String Email;
    @SerializedName("password")
    private String Password;
    @SerializedName("phone")
    private int Phone;
    @SerializedName("isExternal")
    private boolean isExternal;

    public boolean isExternal() {
        return isExternal;
    }

    public void setExternal(boolean external) {
        isExternal = external;
    }

    @SerializedName("userTag")
    private ArrayList<UserTagModelIn> Tags;

    public UserModelIn() {
    }

    public UserModelIn(String _email) {
        Email = _email;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "Name: " + Name + '\n' +
                "Email: " + Email + '\n' +
                "Phone: " + Phone;
    }

    public ArrayList<UserTagModelIn> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<UserTagModelIn> tags) {
        Tags = tags;
    }
}
