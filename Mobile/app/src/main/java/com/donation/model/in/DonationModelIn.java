package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DonationModelIn {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String Description;
    @SerializedName("creatorUser")
    private UserModelIn creatorUser;
    @SerializedName("confirmedUser")
    private UserModelIn confirmedUser;
    @SerializedName("address")
    private AddressModelIn address;
    @SerializedName("state")
    private StateModelIn state;
    @SerializedName("files")
    private ArrayList<FileModelIn> files;

    public DonationModelIn() {
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public UserModelIn getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserModelIn creatorUser) {
        this.creatorUser = creatorUser;
    }

    public UserModelIn getConfirmedUser() {
        return confirmedUser;
    }

    public void setConfirmedUser(UserModelIn confirmedUser) {
        this.confirmedUser = confirmedUser;
    }

    public AddressModelIn getAddress() {
        return address;
    }

    public void setAddress(AddressModelIn address) {
        this.address = address;
    }

    public StateModelIn getState() {
        return state;
    }

    public void setState(StateModelIn state) {
        this.state = state;
    }

    public ArrayList<FileModelIn> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileModelIn> files) {
        this.files = files;
    }
}
