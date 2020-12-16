package com.donation.model.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DonationEventModelOut implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("validityfrom")
    @Expose
    private String validityFrom;
    @SerializedName("validityuntil")
    @Expose
    private String validityUntil;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("centers")
    @Expose
    private List<AddressModelOut> centers;
    @SerializedName("creatoruser")
    @Expose
    private UserModelOut creatorUser;

    public DonationEventModelOut() {
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

    public String getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(String validityUntil) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AddressModelOut> getCenters() {
        return centers;
    }

    public void setCenters(List<AddressModelOut> centers) {
        this.centers = centers;
    }

    public UserModelOut getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserModelOut creatorUser) {
        this.creatorUser = creatorUser;
    }
}
