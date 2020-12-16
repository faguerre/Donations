package com.donation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.donation.model.AddressModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<AddressModel> selected = new MutableLiveData<AddressModel>();

    public void setAddress(AddressModel item) {
        selected.setValue(item);
    }

    public LiveData<AddressModel> getAddress() {
        return selected;
    }
}