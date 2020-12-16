package com.donation.donation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.donation.model.in.DonationModelIn;

public class DonationViewModel extends ViewModel {

    private final MutableLiveData<DonationModelIn> viewModelDonation = new MutableLiveData<>();

    public void setDonation(DonationModelIn donationModelIn) {
        viewModelDonation.setValue(donationModelIn);
    }

    public LiveData<DonationModelIn> getDonation() {
        return viewModelDonation;
    }
}

