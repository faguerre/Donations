package com.donation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CenterAddressModel implements Parcelable, Serializable {
    @SerializedName("markers")
    private List<AddressModel> markers;


    public CenterAddressModel() {
        markers = new ArrayList<>();
    }

    public void CenterAddressModel(List<AddressModel> addressModelList) {
        markers = addressModelList;
    }

    public List<AddressModel> getMarkers() {
        return markers;
    }

    public void setMarkers(List<AddressModel> markers) {
        this.markers = markers;
    }

    public void addMarker(AddressModel addressModel) {
        this.getMarkers().add(addressModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(markers);
    }

    public CenterAddressModel(Parcel in) {
        markers = in.createTypedArrayList(AddressModel.CREATOR);
    }

    public static final Creator<CenterAddressModel> CREATOR = new Creator<CenterAddressModel>() {
        @Override
        public CenterAddressModel createFromParcel(Parcel in) {
            return new CenterAddressModel(in);
        }

        @Override
        public CenterAddressModel[] newArray(int size) {
            return new CenterAddressModel[size];
        }
    };
}
