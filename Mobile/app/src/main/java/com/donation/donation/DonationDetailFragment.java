package com.donation.donation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.donation.R;
import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.donation.DonationUtils.DonationViewModel;
import com.donation.donation.DonationUtils.ImageAdapter;
import com.donation.model.in.DonationModelIn;
import com.donation.model.in.UserModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DonationDetailFragment extends Fragment {


    TextView txtDetailDonationName;
    TextView txtDetailDonationDescription;
    TextView txtDonationCreatorUser;
    TextView txtDonationStatus;
    TextView txtLaunchMapLabel;
    TextView txtDonationConfirmedUser;
    TextView txtLabelDetailDonationConfirmed;
    TextView txtLabelDetailDonationCreator;
    Button btn_ConfirmDonation;
    Button btn_DeleteDonation;
    Button btn_launchMap;
    Button btn_ExtendDonationTime;
    private DonationViewModel viewModelDonation;
    DonationModelIn donationModelIn;
    ViewPager viewPager;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    LatLng latLng;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList = null;
    LoginDBHandler loginDBHandler;
    UserModelIn userLoggedIn;
    Retrofit retrofit;
    ApiServiceDonation apiServiceDonation;
    TokenHandler tokenHandler;

    public DonationDetailFragment() {
        // Required empty public constructor
        retrofit = ApiClient.getClient();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_detail, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        loginDBHandler = new LoginDBHandler(getContext(), null, null, 1);
        tokenHandler = new TokenHandler(getContext(), null, null, 1);
        userLoggedIn = loginDBHandler.getUser();
        apiServiceDonation = retrofit.create(ApiServiceDonation.class);

        SetCurrentLocation();

        txtDetailDonationDescription = view.findViewById(R.id.txtDetailDonationDescription);
        txtLaunchMapLabel = view.findViewById(R.id.txtLabelDetailDonationDistance);
        txtDetailDonationName = view.findViewById(R.id.txtDetailDonationName);
        txtDonationStatus = view.findViewById(R.id.txtDonationStatus);
        txtDonationCreatorUser = view.findViewById(R.id.txtUserCreator);
        txtDonationConfirmedUser = view.findViewById(R.id.txtUserConfirmed);
        txtLabelDetailDonationConfirmed = view.findViewById(R.id.txtLabelDetailDonationConfirmed);
        txtLabelDetailDonationCreator = view.findViewById(R.id.txtLabelDetailDonationCreator);
        btn_ConfirmDonation = view.findViewById(R.id.btn_confirmDetailDonation);
        btn_launchMap = view.findViewById(R.id.btn_DetailDonationGoogleMap);
        btn_ExtendDonationTime = view.findViewById(R.id.btn_ExtendDonation);
        btn_DeleteDonation = view.findViewById(R.id.btn_DeleteDonation);
        viewModelDonation = new ViewModelProvider(getActivity()).get(DonationViewModel.class);
        donationModelIn = viewModelDonation.getDonation().getValue();

        viewPager = view.findViewById(R.id.id_viewPagerDetailDonation);
        ImageAdapter adapter = new ImageAdapter(getContext(), donationModelIn.getFiles());
        viewPager.setAdapter(adapter);

        if (donationModelIn != null) {
            if (donationModelIn.getConfirmedUser() != null) {
                txtDonationConfirmedUser.setText(donationModelIn.getConfirmedUser().toString());
            }
            txtDetailDonationDescription.setText(donationModelIn.getDescription());
            txtDetailDonationName.setText(donationModelIn.getName());
            txtDonationCreatorUser.setText(donationModelIn.getCreatorUser().toString());
            txtDonationStatus.setText(donationModelIn.getState().getActualState());
        }

        ButtonActionController(donationModelIn, userLoggedIn);

        btn_launchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String donationAddress = donationModelIn.getAddress().getName();
                DisplayTrack(donationAddress, addressList.get(0));
            }
        });

        btn_ExtendDonationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExtendDonationTime(view);
            }
        });

        btn_DeleteDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDonation(view);
            }
        });

        btn_ConfirmDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!userLoggedIn.getEmail().equals(donationModelIn.getCreatorUser().getEmail())) {
                    Call<String> call = apiServiceDonation.ConfirmDonation(tokenHandler.getUserToken(), donationModelIn.getId());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String _strin = response.body();
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Success!")
                                    .setMessage("")
                                    .setIcon(getResources().getDrawable(R.drawable.small_logo))
                                    .show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "User creator and user who wants the donation are the same.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void DeleteDonation(final View view) {
        Call<String> call = apiServiceDonation.Delete(donationModelIn.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                new AlertDialog.Builder(getContext())
                        .setTitle("Success!")
                        .setMessage(_response)
                        .setIcon(getResources().getDrawable(R.drawable.small_logo))
                        .show();
                Navigation.findNavController(view).popBackStack(R.id.userDonationsViewFragment, false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ExtendDonationTime(final View view) {
        Call<String> call = apiServiceDonation.ExtendDonation(donationModelIn.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                new AlertDialog.Builder(getContext())
                        .setTitle("Success!")
                        .setMessage(response.body())
                        .setIcon(getResources().getDrawable(R.drawable.small_logo))
                        .show();
                Navigation.findNavController(view).popBackStack(R.id.userDonationsViewFragment, false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Ups!")
                        .setMessage(t.getMessage())
                        .setIcon(getResources().getDrawable(R.drawable.small_logo)).show();
            }
        });
    }

    private void ButtonActionController(DonationModelIn donationModelIn, UserModelIn userLoggedIn) {

        if (donationModelIn.getConfirmedUser() != null) {
            btn_DeleteDonation.setVisibility(View.GONE);
            btn_ExtendDonationTime.setVisibility(View.GONE);
            btn_ConfirmDonation.setVisibility(View.GONE);
        } else {
            if (userLoggedIn == null) {
                btn_launchMap.setVisibility(View.GONE);
                btn_ConfirmDonation.setVisibility(View.GONE);
                txtLaunchMapLabel.setVisibility(View.GONE);
                btn_DeleteDonation.setVisibility(View.GONE);
                btn_ExtendDonationTime.setVisibility(View.GONE);
                txtLabelDetailDonationConfirmed.setVisibility(View.GONE);
                txtDonationConfirmedUser.setVisibility(View.GONE);
            } else {
                if (donationModelIn != null && userLoggedIn != null) {
                    if (donationModelIn.getCreatorUser().getEmail().equals(userLoggedIn.getEmail())) {
                        if (donationModelIn.getState().isWasExtended() || donationModelIn.getState().getDaysLeft() > 0) {
                            btn_ExtendDonationTime.setEnabled(false);
                            btn_ExtendDonationTime.setVisibility(View.GONE);
                        }
                        btn_ConfirmDonation.setEnabled(false);
                        btn_launchMap.setEnabled(false);
                        btn_launchMap.setVisibility(View.GONE);
                        btn_ConfirmDonation.setVisibility(View.GONE);
                        txtLaunchMapLabel.setVisibility(View.GONE);
                        txtLabelDetailDonationConfirmed.setVisibility(View.GONE);
                        txtDonationConfirmedUser.setVisibility(View.GONE);
                        txtLabelDetailDonationCreator.setVisibility(View.GONE);
                        txtDonationCreatorUser.setVisibility(View.GONE);
                    } else {
                        btn_DeleteDonation.setEnabled(false);
                        btn_DeleteDonation.setVisibility(View.GONE);
                        btn_ExtendDonationTime.setEnabled(false);
                        btn_ExtendDonationTime.setVisibility(View.GONE);
                        txtLabelDetailDonationConfirmed.setVisibility(View.GONE);
                        txtDonationConfirmedUser.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void DisplayTrack(String donationAddress, Address currentAddress) {
        try {
            String locationNameRelated = "";
            locationNameRelated = currentAddress.getAddressLine(0);
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + locationNameRelated + "/" + donationAddress);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

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
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}