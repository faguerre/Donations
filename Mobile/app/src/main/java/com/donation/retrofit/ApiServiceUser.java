package com.donation.retrofit;

import com.donation.model.in.UserModelIn;
import com.donation.model.out.UserModelOut;
import com.donation.model.out.UserTagModelOut;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServiceUser {

    @GET("api/users")
    Call<List<UserModelIn>> getAllUsers();

    @GET("api/users/{id}")
    Call<UserModelIn> getUserById(@Path("id") int id);

    @DELETE("api/users/{id}")
    Call<ResponseBody> deleteUserById(@Path("id") int id);

    @POST("api/users")
    Call<UserModelIn> addUser(@Body UserModelOut user);

    @PUT("api/users/{id}")
    Call<UserModelIn> editUser(@Path("id") int id, @Body UserModelOut user);

    @POST("api/users/tags")
    Call<ResponseBody> addTags(@Body UserTagModelOut userTag);
}
