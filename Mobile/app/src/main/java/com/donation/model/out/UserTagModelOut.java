package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserTagModelOut {
    @SerializedName("token")
    @Expose
    private String Token;
    @SerializedName("tags")
    @Expose
    private ArrayList<String> Tags;
    @SerializedName("userTags")
    @Expose
    private ArrayList<String> UserTags;

    public UserTagModelOut() {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }

    public ArrayList<String> getUserTags() {
        return UserTags;
    }

    public void setUserTags(ArrayList<String> tags) {
        UserTags = tags;
    }
}
