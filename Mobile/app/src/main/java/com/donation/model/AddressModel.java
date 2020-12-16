package com.donation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressModel implements Serializable, Parcelable {

    @SerializedName("name")
    private String Name;
    @SerializedName("latitute")
    private String Latitute;
    @SerializedName("longitute")
    private String Longitute;

    public AddressModel () {}

    public AddressModel (String name, String latitute, String longitute) {
        Name = name;
        Latitute = latitute;
        Longitute = longitute;
    }

    protected AddressModel(Parcel in) {
        Name = in.readString();
        Latitute = in.readString();
        Longitute = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatitute() {
        return Latitute;
    }

    public void setLatitute(String latitute) {
        Latitute = latitute;
    }

    public String getLongitute() {
        return Longitute;
    }

    public void setLongitute(String longitute) {
        Longitute = longitute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Latitute);
        parcel.writeString(Longitute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel that = (AddressModel) o;
        return Latitute.equals(that.Latitute) &&
                Longitute.equals(that.Longitute);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
