package com.denny.encapsulationsample.model;

import android.content.Context;
import android.util.Log;

import com.denny.encapsulationsample.contract.MainContract;
import com.denny.encapsulationsample.net.IRequestCallback;
import com.denny.encapsulationsample.net.IRequestManager;
import com.denny.encapsulationsample.net.RequestFactory;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:54
 */
public class MainModel implements MainContract.IMainModel {
    String url = "http://192.168.200.91/demoapi/api/gettemp";

    private MainContract.IMainPresenter iMainPresenter;
    // http://localhost/demoapi/api/gettemp

    public void GetData(Context context) {
        IRequestManager manager = RequestFactory.getRequestManager(context);
        manager.get(url, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("onSuccess", response);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("onFailure", throwable.toString());

            }
        });
    }
}
