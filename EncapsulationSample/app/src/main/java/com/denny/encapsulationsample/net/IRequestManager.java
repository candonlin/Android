package com.denny.encapsulationsample.net;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:56
 */

/*

 * 此接口提供的就是http請求通用的方法，該接口可以用Volley來實現，也能用OkHttp等其它方式來實現

 * 接口說明：

 * get方法參數包含一個url，以及返回數據的接口

 * post/put/delete方法還需要提供一個請求體參數

 */
public interface IRequestManager {
    void  get (String url, IRequestCallback requestCallback) ;

    void  post (String url, String requestBodyJson, IRequestCallback requestCallback) ;

    void  put (String url, String requestBodyJson, IRequestCallback requestCallback) ;

    void  delete (String url, String requestBodyJson, IRequestCallback requestCallback) ;

}