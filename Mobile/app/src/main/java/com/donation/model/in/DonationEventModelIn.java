package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonationEventModelIn implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("validityfrom")
    private String validityFrom;
    @SerializedName("validityuntil")
    private UserModelIn validityUntil;
    @SerializedName("description")
    private String description;
    @SerializedName("email")
    private String email;
    @SerializedName("active")
    private boolean active;
    @SerializedName("phone")
    private String phone;
    @SerializedName("centers")
    private List<AddressModelIn> centers;

    public DonationEventModelIn() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidityFrom() {
        return validityFrom;
    }

    public void setValidityFrom(String validityFrom) {
        this.validityFrom = validityFrom;
    }

    public UserModelIn getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(UserModelIn validityUntil) {
        this.validityUntil = validityUntil;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AddressModelIn> getCenters() {
        return centers;
    }

    public void setCenters(List<AddressModelIn> centers) {
        this.centers = centers;
    }

    public ArrayList<String> getCentersName() {
        ArrayList<String> centersName = new ArrayList<>();

        for (AddressModelIn center : centers) {
            centersName.add(center.getName());
        }
        return centersName;
    }
}
