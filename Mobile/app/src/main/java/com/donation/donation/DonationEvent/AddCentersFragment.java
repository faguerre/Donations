package com.donation.donation.DonationEvent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.donation.R;
import com.donation.model.AddressModel;
import com.donation.model.CenterAddressModel;
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


public class AddCentersFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    Button btnBackToEvent, btnclearMarkers;
    SupportMapFragment mapFragment;
    CenterAddressModel centerAddressModel = new CenterAddressModel();
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    SearchView searchView;
    AddressModel addressModel;
    Address address;
    List<Address> addressList;
    private static final int REQUEST_CODE = 101;


    public AddCentersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_centers, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.addCenter_google_map);
        SetCurrentLocation();
        btnBackToEvent = view.findViewById(R.id.btnBackToEvent);
        btnclearMarkers = view.findViewById(R.id.btnclearMarkers);
        searchView = view.findViewById(R.id.searchEventMap);
        btnBackToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (centerAddressModel.getMarkers().size() == 0) {
                    Toast.makeText(getContext(), R.string.NullCenters, Toast.LENGTH_LONG).show();
                } else {
                    Bundle bundl = new Bundle();
                    bundl.putSerializable("centerAddressModel", centerAddressModel);

                    AddCentersFragmentDirections.ActionAddCentersFragmentToCreateEventFragment action = AddCentersFragmentDirections.actionAddCentersFragmentToCreateEventFragment(centerAddressModel);
                    action.setCentersAddresses(centerAddressModel);
                    closeKeyboard();
                    Navigation.findNavController(view).navigate(action);
                }
            }
        });
        btnclearMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.clear();
                        closeKeyboard();
                    }
                });
            }
        });

        SetCurrentLocation();
        searchLocation();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        Geocoder geocoder = new Geocoder(getContext());
                        String name = "";
                        try {
                            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                            if (addressList.size() == 0) {
                                Toast.makeText(getContext(), R.string.SearchNull, Toast.LENGTH_LONG).show();
                            } else {
                                address = addressList.get(0);
                                if (address.getThoroughfare() == null) {
                                    Toast.makeText(getContext(), R.string.SearchNull, Toast.LENGTH_LONG).show();

                                } else {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                    googleMap.addMarker(markerOptions);

                                    name = getAddressRelatedToUbication(latLng.latitude, latLng.longitude);
                                    addCenterToEvent(name);
                                }
                            }
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.SearchNull, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return view;
    }

    private String getAddressRelatedToUbication(double latitude, double longitude) {
        String locationNameRelated = "Not found";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            locationNameRelated = obj.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return locationNameRelated;
    }

    private void addCenterToEvent(String name) {
        addressModel = new AddressModel(name, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
        if (!ExistsMarker(addressModel)) {
            centerAddressModel.addMarker(addressModel);
        }
    }

    private boolean ExistsMarker(AddressModel addressModel) {
        return centerAddressModel.getMarkers().contains(addressModel);
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
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Ud está aquí");
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
                addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                        if (addressList == null || addressList.size() == 0) {
                            Toast.makeText(getContext(), R.string.SearchNull, Toast.LENGTH_LONG).show();
                        } else {
                            address = addressList.get(0);
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                    googleMap.addMarker(markerOptions);
                                    String name = ((address.getThoroughfare() != null) ? address.getThoroughfare() : "Centro de recepción")
                                            + "  " + ((address.getSubThoroughfare() != null) ? address.getSubThoroughfare() : "");

                                    addCenterToEvent(name);
                                }
                            });
                        }
                    } catch (IOException e) {
                        Toast.makeText(getContext(), R.string.SearchNull, Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        map.setMyLocationEnabled(true);
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
