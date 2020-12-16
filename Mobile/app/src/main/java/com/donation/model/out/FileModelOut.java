package com.donation.model.out;

import com.google.gson.annotations.SerializedName;

public class FileModelOut {
    @SerializedName("Name")
    private String Name;

    @SerializedName("FileBase64")
    private String FileBase64;

    public FileModelOut() {
    }

    public FileModelOut(String name, String fileBase64) {
        Name = name;
        FileBase64 = fileBase64;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFileBase64() {
        return FileBase64;
    }

    public void setFileBase64(String fileBase64) {
        FileBase64 = fileBase64;
    }
}
