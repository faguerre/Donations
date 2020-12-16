package com.donation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.donation.model.AddressModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationMapFragment extends Fragment {


    GoogleMap map;
    SearchView searchView;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;
    LatLng latLng;
    Button buttonConfirm;
    private SharedViewModel addressModelView;
    private static final int REQUEST_CODE = 101;
    private PageViewModel viewModel;

    public LocationMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        searchView = view.findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        buttonConfirm = view.findViewById(R.id.btn_confirmLocation);
        SetCurrentLocation();
        searchLocation();
        SetAddressSelectedInMap();


        viewModel = new ViewModelProvider(getActivity()).get(PageViewModel.class);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressModel addressModel = new AddressModel("", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                String _address = getAddressRelatedToUbication(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                addressModel.setName(_address);
                LocationMapFragmentDirections.ActionNavLocationMapFragmentToDonationFragment action = LocationMapFragmentDirections.actionNavLocationMapFragmentToDonationFragment(addressModel);
                action.setAddressArgsModel(addressModel);
                viewModel.setName(addressModel);
                Navigation.findNavController(view).popBackStack(R.id.donationFragment, false);
            }
        });
        return view;
    }

    private void SetAddressSelectedInMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng _latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(_latLng);
                        markerOptions.title(_latLng.latitude + ":" + _latLng.longitude);
                        latLng = new LatLng(_latLng.latitude, _latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
    }


    private String getAddressRelatedToUbication(String latitude, String longitude) {
        String locationNameRelated = "Not Found";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            double lat = Double.valueOf(latitude);
            double longi = Double.valueOf(longitude);
            List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
            Address obj = addresses.get(0);
            locationNameRelated = obj.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return locationNameRelated;
    }

    private void SetCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }
            }
        });
    }

    private void searchLocation() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        //aca se puede poner n centros
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SetCurrentLocation();
                }
                break;
        }
    }
}