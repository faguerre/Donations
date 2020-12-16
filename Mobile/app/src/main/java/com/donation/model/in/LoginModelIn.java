package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class LoginModelIn {

    @SerializedName("token")
    private String Token;

    public LoginModelIn() {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
