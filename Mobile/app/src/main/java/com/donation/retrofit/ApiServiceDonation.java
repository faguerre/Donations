package com.donation.retrofit;

import com.donation.model.in.DonationModelIn;
import com.donation.model.out.DonationModelOut;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiServiceDonation {
    @POST("api/donations")
    Call<ResponseBody> AddDonation(@Body DonationModelOut donation);

    @GET("api/donations/test")
    Call<ResponseBody> GetTest();

    @GET("api/donations/{id}")
    Call<DonationModelIn> Get(@Path("id") int id);

    @GET("api/donations")
    Call<ArrayList<DonationModelIn>> getAllDonations(@Header("Authorization") String token, @Query("active") boolean own, @Query("confirmed") boolean confirmed);

    @POST("api/donations/{id}")
    Call<String> ConfirmDonation(@Header("Authorization") String token, @Path("id") int id);

    @PUT("api/donations/{id}")
    Call<String> ExtendDonation(@Path("id") int id);

    @DELETE("api/donations/{id}")
    Call<String> Delete(@Path("id") int id);

    @GET("api/donations/user/{token}")
    Call<ArrayList<DonationModelIn>> GetDonationUserTags(@Path("token") String token);

    @GET("api/donations/user/{token}")
    Call<ArrayList<DonationModelIn>> GetConfirmedDonations(@Path("token") String token);
}
