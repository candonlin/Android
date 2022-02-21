package com.denny.loginsample.model;

import com.denny.loginsample.contract.MainContract;
import com.denny.loginsample.retrofit.LoginApiService;
import com.denny.loginsample.retrofit.response.LoginResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * author: Denny
 * created on: 2021/2/2 上午 10:40
 */
public class MainModel implements MainContract.IMainModel {
    private String BaseUrl = "http://192.168.200.91/RegalStudy/";
    private MainContract.IMainPresenter iMainPresenter;

    public MainModel(MainContract.IMainPresenter iMainPresenter) {
        this.iMainPresenter = iMainPresenter;
    }

    private String getBaseUrl() {
        return BaseUrl;
    }

    @Override
    public void checkInfo(final String a, final String p) {
        Retrofit retrofit = null;
        try {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            LoginApiService apiService = retrofit.create(LoginApiService.class);

            JSONObject data = new JSONObject();
            data.put("EMPNO", a);
            data.put("EMPPASSWORD", p);
            Call<LoginResponse> call = apiService.loginVerification(data.toString());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().code.equals("1")) {
                            iMainPresenter.checkResult(true, -1, response.body().data.EMPNAME);
                        } else {
                            iMainPresenter.checkResult(false, 1, response.body().errmsg);

                        }
                    } else {
                        try {
                            iMainPresenter.showErrMsg(false, 0, "Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
                        }

                    }
                    iMainPresenter.closeProgress();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    iMainPresenter.closeProgress();
                    iMainPresenter.showErrMsg(false, 0, "Error", t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
