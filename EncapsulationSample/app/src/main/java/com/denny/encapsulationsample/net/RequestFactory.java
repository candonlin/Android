package com.denny.encapsulationsample.net;

import android.content.Context;

import com.denny.encapsulationsample.net.okhttp.OkHttpRequestManager;
import com.denny.encapsulationsample.net.volley.VolleyRequestManager;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:59
 */

/*

 * 該類的作用是用於返回一個IRequestManager對象，這個IRequestManager的實現類

 * 可以是使用Volley實現的http請求對象，也可以是OkHttp實現的http請求對象

 * Activity/Fragment/Presenter中，只要調用getRequestManager()方法就能得到

 * http請求的操作接口，而不用關心具體是使用什麼實現的。

 */

public class RequestFactory {
    public static IRequestManager getRequestManager(Context context) {
        return VolleyRequestManager.getInstance(context);
//return OkHttpRequestManager.getInstance(context);

    }

}
