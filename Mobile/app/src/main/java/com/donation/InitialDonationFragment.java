package com.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class InitialDonationFragment extends Fragment {

    public InitialDonationFragment() {
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
        return inflater.inflate(R.layout.fragment_initial_donation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCreateDonation = view.findViewById(R.id.btnCreateDonation);
        Button btnViewAllDonations = view.findViewById(R.id.btnDonationView);

        final NavController navigation = Navigation.findNavController(view);

        btnCreateDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.donationsViewFragment);
            }
        });
    }
}