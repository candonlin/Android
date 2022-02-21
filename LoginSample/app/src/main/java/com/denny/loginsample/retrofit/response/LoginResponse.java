package com.denny.loginsample.retrofit.response;

/**
 * author: Denny
 * created on: 2021/2/2 下午 04:27
 */
public class LoginResponse {
    public String code = "";
    public Data data;
    public String errmsg = "";

    public class Data {
        public String EMPNAME = "";
    }

}
