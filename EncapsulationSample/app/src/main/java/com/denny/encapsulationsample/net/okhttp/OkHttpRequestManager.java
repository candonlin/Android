package com.denny.encapsulationsample.net.okhttp;

import android.content.Context;

import com.denny.encapsulationsample.net.IRequestCallback;
import com.denny.encapsulationsample.net.IRequestManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: Denny
 * created on: 2021/4/16 下午 02:02
 */
public class OkHttpRequestManager implements IRequestManager {
    private Context context;

    public static OkHttpRequestManager getInstance(Context context) {
        return new OkHttpRequestManager(context);
    }

    public OkHttpRequestManager(Context context) {
        this.context = context;
    }

    @Override
    public void get(String url, final IRequestCallback requestCallback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallback.onFailure(e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestCallback.onSuccess(response.body().string());
            }
        });


    }

    @Override

    public void post(String url, String requestBodyJson, IRequestCallback requestCallback) {
//        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        addCallBack(requestCallback, request);
    }

    @Override
    public void put(String url, String requestBodyJson, IRequestCallback requestCallback) {
    }

    @Override
    public void delete(String url, String requestBodyJson, IRequestCallback requestCallback) {
    }

}
