package com.donation.donation.DonationEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.R;
import com.donation.donation.DonationEvent.AdapterDonationEvent;
//import com.donation.donationEvent.EventViewFragmentDirections;
import com.donation.model.in.DonationEventModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonationEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EventViewFragment extends Fragment {
    List<DonationEventModelIn> donationEvents;
    RecyclerView recyclerView;

    public EventViewFragment() {
        donationEvents = new ArrayList<DonationEventModelIn>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void generateList() {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceDonationEvent donationEventService = retrofit.create(ApiServiceDonationEvent.class);
        Call<List<DonationEventModelIn>> call = donationEventService.getAllDonationEvents();
        call.enqueue(new Callback<List<DonationEventModelIn>>() {
            @Override
            public void onResponse(Call<List<DonationEventModelIn>> call, Response<List<DonationEventModelIn>> response) {

                donationEvents = response.body();
                AdapterDonationEvent adapter = new AdapterDonationEvent((ArrayList<DonationEventModelIn>) donationEvents);
                recyclerView.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DonationEventModelIn event = donationEvents.get(recyclerView.getChildAdapterPosition(v));
                        Bundle bundl = new Bundle();
                        bundl.putSerializable("eventToDonate", event);
                        EventViewFragmentDirections.ActionNavEventViewFragmentToDonateToEvent action = EventViewFragmentDirections.actionNavEventViewFragmentToDonateToEvent(event);
                        action.setEventToDonate(event);
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<DonationEventModelIn>> call, Throwable t) {
                Log.println(Log.ERROR, "DonationEvent", (t.getMessage() == null ? "Error inesperado" : t.getMessage()));
                Toast.makeText(getContext(), getResources().getString(R.string.NoActiveEvents), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_event_view, container, false);

        recyclerView = view.findViewById(R.id.recyclereventid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        generateList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}