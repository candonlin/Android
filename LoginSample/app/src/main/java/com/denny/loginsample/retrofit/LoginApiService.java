package com.denny.loginsample.retrofit;

import com.denny.loginsample.retrofit.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * author: Denny
 * created on: 2021/2/2 下午 04:27
 */
public interface LoginApiService {

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<LoginResponse> loginVerification(@Body String body);
}
