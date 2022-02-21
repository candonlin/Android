package com.denny.apisample.retrofit;

import com.denny.apisample.retrofit.response.BaseResult;
import com.denny.apisample.retrofit.response.InvInfo;
import com.denny.apisample.retrofit.response.ProductInfo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * author: Denny
 * created on: 2021/2/3 下午 02:28
 */
public interface ApiService {
    @GET("api/getinfo_product")
    Call<BaseResult<ProductInfo>> getinfo_product();

    @GET("api/getinfo_inv")
    Call<BaseResult<InvInfo>> getinfo_inv();

    @POST("api/UploadAnyFile")
    Call<String> uploadFile(@Body RequestBody body);
}
