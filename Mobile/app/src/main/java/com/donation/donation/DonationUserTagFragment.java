package com.donation.donation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.donation.R;
import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.model.in.DonationModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonationUserTagFragment extends Fragment {
    List<DonationModelIn> donations;
    RecyclerView recyclerView;
    DonationModelIn donationModelIn;
    private DonationViewModel donationViewModel;
    LoginDBHandler userLoggedDBHandler;
    TokenHandler tokenHandler;

    public DonationUserTagFragment() {
        donations = new ArrayList<DonationModelIn>();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_donations_view, container, false);
        donationViewModel = new ViewModelProvider(getActivity()).get(DonationViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        userLoggedDBHandler = new LoginDBHandler(getContext(), null, null, 1);
        tokenHandler = new TokenHandler(this.getContext(), null, null, 1);
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
        String token = tokenHandler.getUserToken();
        Call<ArrayList<DonationModelIn>> call = donationService.GetDonationUserTags(token);

        call.enqueue(new Callback<ArrayList<DonationModelIn>>() {
            @Override
            public void onResponse(Call<ArrayList<DonationModelIn>> call, Response<ArrayList<DonationModelIn>> response) {
                donations = response.body();
                AdapterDonationRB adapter = new AdapterDonationRB((ArrayList<DonationModelIn>) donations, userLoggedDBHandler.getUser());
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
                        .setMessage("No cuentas con favoritos en tu lista")
                        .setIcon(getResources().getDrawable(R.drawable.small_logo))
                        .setPositiveButton("Agrega favoritos", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Navigation.findNavController(view).navigate(R.id.tagFragment);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Navigation.findNavController(view).navigate(R.id.nav_homeFragment);
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}