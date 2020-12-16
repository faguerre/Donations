package com.donation.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.donation.utils.DonationConstants;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {
    PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
    }

    public Geofence getGeofence(String id, LatLng latLng, int transitionType) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, DonationConstants.GEOFENCE_RADIUS_IN_METERS)
                .setRequestId(id)
                .setTransitionTypes(transitionType)
                .setLoiteringDelay(1000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent() {

        if (pendingIntent == null) {
            Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return pendingIntent;
    }

    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "Servicio no disponible";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Se llego al limite de alertas";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Reintente luego";
            }
        }
        return e.getLocalizedMessage();
    }
}
