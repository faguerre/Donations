package com.donation.retrofit;

import com.donation.model.in.DonationEventModelIn;
import com.donation.model.out.DonationEventModelOut;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServiceDonationEvent {

    @GET("api/events")
    Call<List<DonationEventModelIn>> getAllDonationsEvents();

    @POST("api/events")
    Call<DonationEventModelOut> addDonationEvent(@Body DonationEventModelOut event);

    @GET("api/events")
    Call<List<DonationEventModelIn>> getAllDonationEvents();
}
