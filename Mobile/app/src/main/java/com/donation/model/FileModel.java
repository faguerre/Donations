package com.donation.model;

import com.google.gson.annotations.SerializedName;

public class FileModel {

    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("size")
    private String Size;
    @SerializedName("dataFiles")
    private String DataFiles;

    public FileModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getDataFiles() {
        return DataFiles;
    }

    public void setDataFiles(String dataFiles) {
        DataFiles = dataFiles;
    }
}
