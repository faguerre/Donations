package com.donation.donation.DonationEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.donation.R;
import com.donation.model.out.AddressModelOut;
import com.donation.model.out.DonationEventModelOut;


public class EventConfirmation extends Fragment {
    TextView txtinfoEvent;
    TextView txtinfocenterEvent;

    DonationEventModelOut event;

    public EventConfirmation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().get("eventconfirmationdata") != null) {
            event = (DonationEventModelOut) getArguments().get("eventconfirmationdata");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtinfoEvent = view.findViewById(R.id.txtinfoevent);
        txtinfocenterEvent = view.findViewById(R.id.txtinfocenterevent);
        if (getArguments().get("eventconfirmationdata") != null) {
            event = (DonationEventModelOut) getArguments().get("eventconfirmationdata");
        }
        if (event != null) {
            txtinfoEvent.setText(getEventInfo());
            txtinfoEvent.setText(getEventInfoCenter());
        }

    }

    private String getEventInfoCenter() {
        String centers = "";
        for (AddressModelOut infoCenter : event.getCenters()) {
            centers = infoCenter.getName() + "\n";
        }
        return "Centros de recepción: \n\n" + centers;
    }

    private String getEventInfo() {
        return "Se ha creado la campaña \n\n\n " + event.getName() +
                " \n\n\n  Estará vigente del \n\n\n" +
                event.getValidityFrom() + " al " + event.getValidityUntil();
    }
}