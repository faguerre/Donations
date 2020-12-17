package com.donation.donation.DonationEvent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.donation.model.out.DonationEventModelOut;

public class EventViewModel extends ViewModel {

    private final MutableLiveData<DonationEventModelOut> selected = new MutableLiveData<DonationEventModelOut>();

    public void setEvent(DonationEventModelOut item) {
        selected.setValue(item);
    }

    public LiveData<DonationEventModelOut> getEvent() {
        return selected;
    }
}