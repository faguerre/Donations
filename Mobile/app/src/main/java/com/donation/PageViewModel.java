package com.donation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.donation.model.AddressModel;

public class PageViewModel extends ViewModel {
    /**
     * Live Data Instance
     */
    private final MutableLiveData<AddressModel> mName = new MutableLiveData<>();

    public void setName(AddressModel addressModel) {
        mName.setValue(addressModel);
    }

    public LiveData<AddressModel> getAddressModel() {
        return mName;
    }
}