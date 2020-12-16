package com.donation.donation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.R;
import com.donation.model.in.DonationModelIn;
import com.donation.model.in.UserModelIn;

import java.util.ArrayList;

public class AdapterDonationRB extends RecyclerView.Adapter<AdapterDonationRB.DonationHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<DonationModelIn> donations;
    UserModelIn userLogged;

    public AdapterDonationRB(ArrayList<DonationModelIn> donations, UserModelIn _user) {
        this.donations = donations;
        userLogged = _user;
    }

    @NonNull
    @Override
    public DonationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdonation, parent, false);
        DonationHolder holder = new DonationHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    //@SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DonationHolder holder, int position) {
        holder.personName.setText(donations.get(position).getName());
        holder.personDescription.setText(donations.get(position).getDescription());
        holder.personPhoto.setImageResource(R.drawable.small_logo); //persons.get(position).photoId)

        if (userLogged != null) {
            if (donations.get(position).getCreatorUser().getEmail().equals(userLogged.getEmail())) {
                if (donations.get(position).getState().getDaysLeft() > 0) {
                    holder.daysLeft.setText("Publicacion activa por: " + donations.get(position).getState().getDaysLeft() + " dias");
                    holder.daysLeft.setTextColor(Color.GREEN);
                } else {
                    if (donations.get(position).getState().getDaysLeft() == 0) {
                        holder.daysLeft.setText("La publicacion finaliza hoy");
                        holder.daysLeft.setTextColor(Color.RED);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (donations == null) {
            return 0;
        } else {
            return donations.size();
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class DonationHolder extends RecyclerView.ViewHolder {

        TextView personName;
        TextView personDescription;
        ImageView personPhoto;
        TextView createdBy;
        TextView daysLeft;

        DonationHolder(View itemView) {
            super(itemView);

            personName = itemView.findViewById(R.id.namedonationitem);
            personDescription = itemView.findViewById(R.id.descriptiondonationitem);
            personPhoto = itemView.findViewById(R.id.imagedonationid);
            createdBy = itemView.findViewById(R.id.id_CreatedByDonationItem);
            daysLeft = itemView.findViewById(R.id.id_DaysLeft);
        }
    }
}
