package com.denny.apisample.retrofit.response;

import java.util.ArrayList;

/**
 * author: Denny
 * created on: 2021/2/3 下午 02:28
 */
public class BaseResult<T> {
    public String code = "";
    public ArrayList<T> data = new ArrayList<>();
    public String errmsg = "";
}
