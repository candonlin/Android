package com.denny.bigdatasample.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import com.denny.bigdatasample.contract.MainContract;
import com.denny.bigdatasample.db.AppDataBase;
import com.denny.bigdatasample.db.dao.MovieDAO;
import com.denny.bigdatasample.db.table.MOVIE;
import com.denny.bigdatasample.presenter.MainPresenter;
import com.denny.bigdatasample.retrofit.MovieApiService;
import com.denny.bigdatasample.retrofit.response.MovieResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
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
 * created on: 2021/2/2 下午 05:39
 */
public class MainModel implements MainContract.IMainModel {
    private String BaseUrl = "http://192.168.200.91/RegalStudy/";
    private MainContract.IMainPresenter iMainPresenter;

    private Context context;

    public MainModel(Context context, MainContract.IMainPresenter iMainPresenter) {
        this.context = context;
        this.iMainPresenter = iMainPresenter;
    }

    private String getBaseUrl() {
        return BaseUrl;
    }

    @Override
    public void getMovies(final MainPresenter.ToType type) {
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
            MovieApiService apiService = retrofit.create(MovieApiService.class);
            iMainPresenter.showProgress();

            Call<MovieResponse> call = apiService.getMovies();
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    try {
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            // Current Thread is Main Thread.
                            Log.e("Callback", " is MainLooper");
                        } else {
                            Log.e("Callback", "not MainLooper");
                        }

                        if (response.isSuccessful()) {
                            if (response.body().code.equals("1")) {
                                //取得所有電影
                                Log.e("Callback", new Date().toGMTString());
                                if (type == MainPresenter.ToType.SELF) {
                                    saveAllMovies(response.body());
                                } else {
                                    saveAllMovies2(response.body());
                                }
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
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
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
    public void deleteDataToContentProvider() {
        Uri uri = Uri.parse(MOVIE_URI);
        context.getContentResolver().delete(uri, null, null);
    }

    /**
     * save data to self db
     *
     * @param response
     */
    private void saveAllMovies(MovieResponse response) {
        if (response.data.size() > 0) {
            MovieDAO dao = AppDataBase.getInstance(context).movieDAO();
            dao.insertRecords(response.data);

            Log.e("Callback", new Date().toGMTString());
            iMainPresenter.showSaveSuccess(response.data.size());
        } else {
            iMainPresenter.showErrMsg(false, 0, "Tip", "No data");
        }
    }

    protected String MOVIE_URI = "content://com.regalscan.contentprovidersample/MOVIE";

    /**
     * save data to contentprovider db
     *
     * @param response
     */
    private void saveAllMovies2(MovieResponse response) {
        Uri uri = Uri.parse(MOVIE_URI);

        if (response.data.size() > 0) {
            int total = response.data.size();
            int process = 0;
            List buffer = new LinkedList<>();
            for (MOVIE movie : response.data) {
                ContentValues values = new ContentValues();
                values.put("Name", movie.Name);
                values.put("sDate", movie.sDate);
                buffer.add(values);
                if (buffer.size() == 500) { // 數量控制原因 在於 底層 binder通訊 有1mb 內容大小限制
                    ContentValues[] bufferDataArray = new ContentValues[buffer.size()];
                    buffer.toArray(bufferDataArray);
                    process += context.getContentResolver().bulkInsert(uri, bufferDataArray);
                    buffer.clear();
                }
            }
            if (buffer.size() > 0) {
                ContentValues[] bufferDataArray = new ContentValues[buffer.size()];
                buffer.toArray(bufferDataArray);
                process += context.getContentResolver().bulkInsert(uri, bufferDataArray);
                buffer.clear();
            }
            Log.e("Callback", new Date().toGMTString());
            iMainPresenter.showSaveSuccess2(total, process);
        } else {
            iMainPresenter.showErrMsg(false, 0, "Tip", "No data");
        }



    }
}
