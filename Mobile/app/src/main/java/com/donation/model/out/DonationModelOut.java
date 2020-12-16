package com.donation.model.out;


import com.donation.model.AddressModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonationModelOut implements Serializable {

    private String Name;
    private String Description;
    private AddressModel Address;
    private int CreatorUserId;
    private List<FileModelOut> FileList;
    private ArrayList<String> Tags;

    public DonationModelOut() {
    }

    public DonationModelOut(String name, String description, AddressModel addressModel) {
        Name = name;
        Description = description;
        Address = addressModel;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setAddress(AddressModel address) {
        Address = address;
    }

    public void setCreatorUserId(int creatorUserId) {
        CreatorUserId = creatorUserId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public AddressModel getAddress() {
        return Address;
    }

    public List<FileModelOut> getFileList() {
        return FileList;
    }

    public void setFileList(List<FileModelOut> fileList) {
        FileList = fileList;
    }

    public int getCreatorUserId() {
        return CreatorUserId;
    }

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }
}