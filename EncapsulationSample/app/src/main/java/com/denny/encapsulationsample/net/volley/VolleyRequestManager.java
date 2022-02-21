package com.denny.encapsulationsample.net.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denny.encapsulationsample.net.IRequestCallback;
import com.denny.encapsulationsample.net.IRequestManager;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:58
 */
public class VolleyRequestManager implements IRequestManager {
    private Context context;

    public static VolleyRequestManager getInstance(Context context) {
        return new VolleyRequestManager(context);
    }

    public VolleyRequestManager(Context context) {
        this.context = context;
    }

    @Override
    public void get(String url, final IRequestCallback requestCallback) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        requestCallback.onSuccess(s);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        requestCallback.onFailure(volleyError);
                    }

                });
        mQueue.add(request);
    }

    @Override
    public void post(String url, String requestBodyJson, final IRequestCallback requestCallback) {
//        requestWithBody(url, requestBodyJson, requestCallback, Request.Method.POST);
    }

    @Override
    public void put(String url, String requestBodyJson, final IRequestCallback requestCallback) {
//        requestWithBody(url, requestBodyJson, requestCallback, Request.Method.PUT);
    }

    @Override
    public void delete(String url, String requestBodyJson, final IRequestCallback requestCallback) {
//        requestWithBody(url, requestBodyJson, requestCallback, Request.Method.DELETE);
    }

}
