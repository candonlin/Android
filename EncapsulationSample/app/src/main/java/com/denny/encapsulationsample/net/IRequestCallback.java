package com.denny.encapsulationsample.net;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:57
 */

/**

 * Created by chenjianwei on 2016/12/11.

 * 請求返回成功/失敗，成功時，把服務器返回的結果回調出去，失敗時回調異常信息

 * onSuccess中的參數類型，當然也可以為JSONObject，這裡只是舉個栗子，可按照實際需求變通

 */

public interface IRequestCallback {
    void  onSuccess (String response) ;

    void  onFailure (Throwable throwable) ;

}