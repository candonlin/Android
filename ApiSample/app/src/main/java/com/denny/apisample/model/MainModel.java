package com.denny.apisample.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.denny.apisample.contract.MainContract;
import com.denny.apisample.retrofit.ApiService;
import com.denny.apisample.retrofit.response.BaseResult;
import com.denny.apisample.retrofit.response.InvInfo;
import com.denny.apisample.retrofit.response.ProductInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * author: Denny
 * created on: 2021/2/3 下午 02:23
 */
public class MainModel implements MainContract.IMainModel {
    public static Context context;

    private MainContract.IMainPresenter iMainPresenter;
    //    private String BaseUrl = "http://192.168.200.91/RegalStudy/";
    private String BaseUrl = "http://192.168.0.42/RegalStudy/";

    public MainModel(MainContract.IMainPresenter iMainPresenter) {
        this.iMainPresenter = iMainPresenter;
    }

    private String getBaseUrl() {
        return BaseUrl;
    }

    public Retrofit provideRetrofit(OkHttpClient client, String BASE_URL, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(Executors.newSingleThreadExecutor()) //set call back is background thread
                .client(client)
                .build();
    }

    @Override
    public void getInfo_Product() {
        iMainPresenter.showProgress();
        Retrofit retrofit = null;

        try {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .callbackExecutor(Executors.newSingleThreadExecutor()) //set call back is background thread
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            iMainPresenter.showProgress();

            Call<BaseResult<ProductInfo>> call = apiService.getinfo_product();
            call.enqueue(new Callback<BaseResult<ProductInfo>>() {
                @Override
                public void onResponse(Call<BaseResult<ProductInfo>> call, Response<BaseResult<ProductInfo>> response) {
                    Log.e("Callback", response.body().data.toString());
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().code.equals("1")) {
                                iMainPresenter.showProductInfo(response.body().data.get(0));
                            } else {
                                iMainPresenter.showErrMsg(false, 0, "Error", response.body().errmsg);
                            }
                        } else {
                            iMainPresenter.showErrMsg(false, 0, "Error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
                    } finally {
                        iMainPresenter.closeProgress();
                    }
//                    response.body().data.get(0).
                }

                @Override
                public void onFailure(Call<BaseResult<ProductInfo>> call, Throwable t) {
                    iMainPresenter.showErrMsg(false, 0, "Error", t.getMessage());
                    iMainPresenter.closeProgress();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
            iMainPresenter.closeProgress();

        }


    }

    @Override
    public void getInfo_Inv() {
        iMainPresenter.showProgress();
        Retrofit retrofit = null;

        try {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .callbackExecutor(Executors.newSingleThreadExecutor()) //set call back is background thread
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            iMainPresenter.showProgress();

            Call<BaseResult<InvInfo>> call = apiService.getinfo_inv();
            call.enqueue(new Callback<BaseResult<InvInfo>>() {
                @Override
                public void onResponse(Call<BaseResult<InvInfo>> call, Response<BaseResult<InvInfo>> response) {
                    Log.e("Callback", response.body().data.toString());
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().code.equals("1")) {
                                iMainPresenter.showInvInfo(response.body().data.get(0));
                            } else {
                                iMainPresenter.showErrMsg(false, 0, "Error", response.body().errmsg);
                            }
                        } else {
                            iMainPresenter.showErrMsg(false, 0, "Error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
                    } finally {
                        iMainPresenter.closeProgress();
                    }
//                    response.body().data.get(0).
                }

                @Override
                public void onFailure(Call<BaseResult<InvInfo>> call, Throwable t) {
                    iMainPresenter.showErrMsg(false, 0, "Error", t.getMessage());
                    iMainPresenter.closeProgress();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
            iMainPresenter.closeProgress();

        }


    }

    @Override
    public void getInfo_ProductByOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url(getBaseUrl() + "/api/getinfo_product")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                iMainPresenter.showErrMsg(false, 0, "錯誤", e.toString());
                iMainPresenter.closeProgress();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    final String body = response.body().string();
                    Gson gson = new Gson();

                    Type type = new TypeToken<BaseResult<ProductInfo>>() {
                    }.getType();
                    BaseResult typeUnits = gson.fromJson(body, type);

                    ArrayList<ProductInfo> arrayList = typeUnits.data;
                    if (arrayList != null) {
                        iMainPresenter.showProductInfo(arrayList.get(0));
                    } else {
                        iMainPresenter.closeProgress();
                        iMainPresenter.showErrMsg(false, 0, "Error", typeUnits.errmsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    iMainPresenter.closeProgress();
                    iMainPresenter.showErrMsg(false, 0, "Error", e.toString());
                } finally {
                }
            }
        });
    }

    @Override
    public void getInfo_InvByOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url(getBaseUrl() + "/api/getinfo_inv")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                iMainPresenter.showErrMsg(false, 0, "錯誤", e.toString());
                iMainPresenter.closeProgress();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    final String body = response.body().string();
                    Gson gson = new Gson();

                    Type type = new TypeToken<BaseResult<InvInfo>>() {
                    }.getType();
                    BaseResult typeUnits = gson.fromJson(body, type);

                    ArrayList<InvInfo> arrayList = typeUnits.data;
                    if (arrayList != null) {
                        iMainPresenter.showInvInfo(arrayList.get(0));
                    } else {
                        iMainPresenter.closeProgress();
                        iMainPresenter.showErrMsg(false, 0, "Error", typeUnits.errmsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    iMainPresenter.closeProgress();
                    iMainPresenter.showErrMsg(false, 0, "Error", e.toString());
                } finally {
                }
            }
        });
    }

    @Override
    public void uploadByRetrofit() {
        String filename = "123.txt";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + filename;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        iMainPresenter.showProgress();
        Retrofit retrofit = null;

        try {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("jsonContent", " JSON 內容 ")
                    .addFormDataPart("", filename,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    file))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .callbackExecutor(Executors.newSingleThreadExecutor()) //set call back is background thread
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            iMainPresenter.showProgress();

            Call<String> call = apiService.uploadFile(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("Callback", response.body());
                    try {
                        if (response.isSuccessful()) {
                            JSONObject object = new JSONObject(response.body());
                            if (object.getString("code").equals("1")) {
                                iMainPresenter.showUploadMessage(object.getString("data"));
                            } else {
                                iMainPresenter.showErrMsg(false, 0, "Error", object.getString("errmsg"));
                            }
                        } else {
                            iMainPresenter.showErrMsg(false, 0, "Error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
                    } finally {
                        iMainPresenter.closeProgress();
                    }
//                    response.body().data.get(0).
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    iMainPresenter.showErrMsg(false, 0, "Error", t.getMessage());
                    iMainPresenter.closeProgress();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            iMainPresenter.showErrMsg(false, 0, "Error", e.getMessage());
            iMainPresenter.closeProgress();

        }

    }

    @Override
    public void uploadByOkhttp() {
        String filename = "123.txt";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + filename;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("jsonContent", " JSON 內容 ")
                .addFormDataPart("", filename,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrl() + "/api/UploadAnyFile")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                iMainPresenter.showErrMsg(false, 0, "錯誤", e.toString());
                iMainPresenter.closeProgress();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    final String body = response.body().string();

                    JSONObject object = new JSONObject(body);
                    if (object.getString("code").equals("1")) {
                        iMainPresenter.showUploadMessage(object.getString("data"));
                    } else {
                        iMainPresenter.showErrMsg(false, 0, "Error", object.getString("errmsg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    iMainPresenter.showErrMsg(false, 0, "Error", e.toString());
                } finally {
                    iMainPresenter.closeProgress();

                }
            }
        });
    }
}
