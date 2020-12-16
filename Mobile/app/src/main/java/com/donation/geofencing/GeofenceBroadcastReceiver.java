package com.donation.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.donation.MenuActivity;
import com.donation.utils.DonationNotification;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    DonationNotification donationNotification;

    @Override
    public void onReceive(Context context, Intent intent) {
        donationNotification = new DonationNotification(context);
        donationNotification.sendHighPriorityNotification("Donaciones", "Ud. se encuentra cerca de una donación", MenuActivity.class);
        //Toast.makeText(context, "Ud. se encuentra cerca de una donación", Toast.LENGTH_LONG).show();
    }
}