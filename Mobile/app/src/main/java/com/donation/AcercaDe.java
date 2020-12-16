package com.donation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.donation.model.in.DonationModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AcercaDe extends Fragment {

    public AcercaDe() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acerca_de, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnVer = view.findViewById(R.id.btnVerUsuarios);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Retrofit retrofit = ApiClient.getClient();
                ApiServiceDonation serviceDonation = retrofit.create(ApiServiceDonation.class);

                Call<DonationModelIn> call = serviceDonation.Get(23);

                call.enqueue(new Callback<DonationModelIn>() {
                    @Override
                    public void onResponse(Call<DonationModelIn> call, Response<DonationModelIn> response) {
                        Toast.makeText(getContext(), "Test1", Toast.LENGTH_LONG).show();
                        try {
                            DonationModelIn donationModelIn = response.body();
                            if (donationModelIn != null) {
                                TextView txt = view.findViewById(R.id.txtUsers);
                                txt.setText(donationModelIn.getName());
                            } else {
                                String error = response.errorBody().string();
                                TextView txt = view.findViewById(R.id.txtUsers);
                                txt.setText(error);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DonationModelIn> call, Throwable t) {
                        Toast.makeText(getContext(), "Fail1", Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.println(Log.ERROR, "Back", t.getMessage());

                    }
                });

            }

        });

    }
}