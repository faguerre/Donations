package com.donation.retrofit;

import com.donation.model.in.TagModelIn;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServiceTag {
    @GET("api/tags")
    Call<ArrayList<TagModelIn>> getAllTags();

    @GET("api/tags/{id}")
    Call<TagModelIn> getTagById(@Path("id") int id);
}
