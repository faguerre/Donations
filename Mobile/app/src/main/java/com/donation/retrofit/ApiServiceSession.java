package com.donation.retrofit;

import com.donation.model.in.LoginModelIn;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.LoginModelExternalOut;
import com.donation.model.out.LoginModelOut;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServiceSession {

    @POST("api/sessions")
    Call<LoginModelIn> Login(@Body LoginModelOut loginModelOut);

    @POST("api/sessions/external")
    Call<LoginModelIn> LoginExternal(@Body LoginModelExternalOut loginModelOut);

    @DELETE("api/sessions/{token}")
    Call<ResponseBody> Logout(@Path("token") String token);

    @GET("api/sessions/{token}")
    Call<UserModelIn> GetUserByToken(@Path("token") String token);
}
