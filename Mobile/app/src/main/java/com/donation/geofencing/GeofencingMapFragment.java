package com.donation.geofencing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.donation.R;
import com.donation.model.AddressModel;
import com.donation.utils.DonationConstants;
import com.donation.utils.DonationInfo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeofencingMapFragment extends Fragment {
    private static final int FINE_LOCATION_REQUEST_CODE = 300;
    private GoogleMap gMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private DonationGeoReferences donationGeoReferences;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            enabledUserLocation();
            if (DonationInfo.getPoints() != null) {
                for (AddressModel addressModel : DonationInfo.getPoints()) {
                    SetPointOnMap(addressModel.getName(), new LatLng(Float.parseFloat(addressModel.getLatitute()), Float.parseFloat(addressModel.getLongitute())));
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_geofencing_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        geofencingClient = LocationServices.getGeofencingClient(getContext());
        geofenceHelper = new GeofenceHelper(getContext());
        donationGeoReferences = new DonationGeoReferences(getContext());
        donationGeoReferences.generateList();

    }

    private void enabledUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    gMap.setMyLocationEnabled(true);
                }
            }
        }
    }


    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(R.color.circleStroke);
        circleOptions.fillColor(R.color.circlefill);
        circleOptions.strokeWidth(4);
        gMap.addCircle(circleOptions);

    }

    private void addMarker(String name, LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        markerOptions.title(name);
        gMap.addMarker(markerOptions);
    }

    private void SetPointOnMap(String name, LatLng latLng) {
        addMarker(name, latLng);
        addCircle(latLng, DonationConstants.GEOFENCE_RADIUS_IN_METERS);
        addGeofence(name, latLng, DonationConstants.GEOFENCE_RADIUS_IN_METERS);
    }


    public void addGeofence(String name, LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(name, latLng, Geofence.GEOFENCE_TRANSITION_DWELL
                | Geofence.GEOFENCE_TRANSITION_ENTER);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Geofence", "Geofence added map");
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
}