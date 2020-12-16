package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class FileModelIn {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private String size;
    @SerializedName("dataFiles")
    private String dataFiles;

    public FileModelIn() {
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(String dataFiles) {
        this.dataFiles = dataFiles;
    }
}
