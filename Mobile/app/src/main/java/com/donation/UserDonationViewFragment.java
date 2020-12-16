package com.donation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.donation.AdapterDonationRB;
import com.donation.donation.DonationViewModel;
import com.donation.donation.RecyclerItemClickListener;
import com.donation.model.in.DonationModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserDonationViewFragment extends Fragment {

    public UserDonationViewFragment() {
        // Required empty public constructor
    }

    List<DonationModelIn> donations;
    RecyclerView recyclerView;
    DonationModelIn donationModelIn;
    private DonationViewModel donationViewModel;
    LoginDBHandler userLoggedDBHandler;
    TokenHandler tokenHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_donation_view, container, false);
        donationViewModel = new ViewModelProvider(getActivity()).get(DonationViewModel.class);
        recyclerView = view.findViewById(R.id.id_donationsView_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        userLoggedDBHandler = new LoginDBHandler(getContext(), null, null, 1);
        tokenHandler = new TokenHandler(getContext(), null, null, 1);
        generateList(view);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        donationModelIn = donations.get(position);
                        donationViewModel.setDonation(donationModelIn);
                        Navigation.findNavController(view).navigate(R.id.donationDetailFragment);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return view;
    }

    private void generateList(final View view) {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceDonation donationService = retrofit.create(ApiServiceDonation.class);
        Log.println(Log.INFO, "Donation", new Date().toString());

        String _tokenAvailable = tokenHandler.getUserToken();
        //si del usuario
        Call<ArrayList<DonationModelIn>> call = donationService.getAllDonations(tokenHandler.getUserToken(), true, false);

        call.enqueue(new Callback<ArrayList<DonationModelIn>>() {
            @Override
            public void onResponse(Call<ArrayList<DonationModelIn>> call, Response<ArrayList<DonationModelIn>> response) {
                //Log.println(Log.INFO,"Donation", "Se carga los datos de la lista " + response.body().size());
                donations = response.body();
                AdapterDonationRB adapter = new AdapterDonationRB(DonationsAvailables(donations), userLoggedDBHandler.getUser());
                recyclerView.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Seleccion " + donations.get(recyclerView.getChildAdapterPosition(v)).getName(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<DonationModelIn>> call, Throwable t) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Ups!")
                        .setMessage("Ya no tienes donaciones activas")
                        .setIcon(getResources().getDrawable(R.drawable.small_logo))
                        .setPositiveButton("Crear nuevas", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Navigation.findNavController(view).navigate(R.id.donationFragment);
                            }
                        })
                        .setNegativeButton("Home", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Navigation.findNavController(view).navigate(R.id.nav_homeFragment);
                            }
                        }).show();
                Log.println(Log.ERROR, "Donation", t.getMessage());
            }
        });
    }

    private ArrayList<DonationModelIn> DonationsAvailables(List<DonationModelIn> donations) {
        ArrayList<DonationModelIn> _donations = new ArrayList<>();
        for (int i = 0; i < donations.size(); i++) {
            if (donations.get(i).getConfirmedUser() == null) {
                _donations.add(donations.get(i));
            }
        }
        return _donations;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}