package com.denny.bigdatasample.retrofit.response;

import com.denny.bigdatasample.db.table.MOVIE;

import java.util.ArrayList;

/**
 * author: Denny
 * created on: 2021/2/2 下午 05:44
 */
public class MovieResponse {
    public String code = "";
    public ArrayList<MOVIE> data = new ArrayList<>();
    public String errmsg = "";

}
