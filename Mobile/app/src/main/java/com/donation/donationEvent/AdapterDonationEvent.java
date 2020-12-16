package com.donation.donationEvent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.R;
import com.donation.model.in.DonationEventModelIn;

import java.util.ArrayList;

public class AdapterDonationEvent extends RecyclerView.Adapter<AdapterDonationEvent.DonationEventHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<DonationEventModelIn> donationsEvents;

    public AdapterDonationEvent(ArrayList<DonationEventModelIn> donationsEvents) {
        this.donationsEvents = donationsEvents;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonationEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemeventdonation, parent, false);
        AdapterDonationEvent.DonationEventHolder holder = new AdapterDonationEvent.DonationEventHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonationEventHolder holder, int position) {
        holder.eventName.setText(donationsEvents.get(position).getName());
        holder.eventDescription.setText(donationsEvents.get(position).getDescription());
        holder.eventPhoto.setImageResource(R.drawable.small_logo); //persons.get(position).photoId)
    }

    @Override
    public int getItemCount() {
        return (donationsEvents == null ? 0 : donationsEvents.size());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class DonationEventHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView eventDescription;
        ImageView eventPhoto;

        DonationEventHolder(View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.namedonationeventitem);
            eventDescription = itemView.findViewById(R.id.descriptiondonationeventitem);
            eventPhoto = itemView.findViewById(R.id.imagedonationeventid);
        }
    }
}
