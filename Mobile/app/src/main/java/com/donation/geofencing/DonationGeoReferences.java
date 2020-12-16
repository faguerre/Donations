package com.donation.geofencing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.donation.database.TokenHandler;
import com.donation.model.AddressModel;
import com.donation.model.in.DonationModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;
import com.donation.utils.DonationInfo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonationGeoReferences extends ContextWrapper {

    List<DonationModelIn> donations;
    List<AddressModel> points;

    private final GeofencingClient geofencingClient;
    private final GeofenceHelper geofenceHelper;
    private TokenHandler tokenHandler;

    public DonationGeoReferences(Context base) {
        super(base);
        geofenceHelper = new GeofenceHelper(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        points = new ArrayList<>();
    }

    public void addGeofence(String name, LatLng latLng) {
        Geofence geofence = geofenceHelper.getGeofence(name, latLng,
                Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER);
        final GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Geofence", "Geofence added ");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = geofenceHelper.getErrorString(e);
                        Log.e("Geofence", error);
                    }
                });
    }

    public void generateList() {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceDonation donationService = retrofit.create(ApiServiceDonation.class);
        tokenHandler = new TokenHandler(this, null, null, 1);
        String _tokenAvailable = tokenHandler.getUserToken();
        Call<ArrayList<DonationModelIn>> call = donationService.getAllDonations(tokenHandler.getUserToken(), false, false);
        call.enqueue(new Callback<ArrayList<DonationModelIn>>() {
            @Override
            public void onResponse(Call<ArrayList<DonationModelIn>> call, Response<ArrayList<DonationModelIn>> response) {

                if (response.body() != null) {
                    Log.println(Log.INFO, "Donation", "Se carga los datos de la lista " + response.body().size());
                    donations = response.body();
                    LatLng latLng;
                    for (DonationModelIn donation : donations) {

                        latLng = new LatLng(Float.parseFloat(donation.getAddress().getLatitute()),
                                Float.parseFloat(donation.getAddress().getLongitute()));
                        addGeofence(donation.getName(), latLng);
                        AddressModel point = new AddressModel();
                        point.setName(donation.getName());
                        point.setLongitute(donation.getAddress().getLongitute());
                        point.setLatitute(donation.getAddress().getLatitute());
                        points.add(point);
                    }
                    DonationInfo.setPoints(points);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DonationModelIn>> call, Throwable t) {
                Log.println(Log.ERROR, "Donation", (t.getMessage() == null ? "Error" : t.getMessage()));

            }
        });
    }

    public List<AddressModel> getPoints() {
        return points;
    }
}
