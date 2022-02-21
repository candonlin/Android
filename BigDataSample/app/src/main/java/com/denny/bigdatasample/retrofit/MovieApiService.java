package com.denny.bigdatasample.retrofit;

import com.denny.bigdatasample.retrofit.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * author: Denny
 * created on: 2021/2/2 下午 05:44
 */
public interface MovieApiService {
    @Headers("Content-Type: application/json")
    @GET("api/getmovies")
    Call<MovieResponse> getMovies();
}
