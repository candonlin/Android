package com.denny.imageloder.model;

import com.denny.imageloder.bean.ImageBean;
import com.denny.imageloder.contract.MainContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: Denny
 * created on: 2022/2/22 上午 10:12
 */
public class MainModel implements MainContract.IMainModel {
    private MainContract.IMainPresenter iMainPresenter;

    public MainModel(MainContract.IMainPresenter iMainPresenter) {
        this.iMainPresenter = iMainPresenter;
    }

    @Override
    public void getImage() {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url("https://jsonplaceholder.typicode.com/photos").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iMainPresenter.closeProgress();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.code()==200){
                        ArrayList<ImageBean> arrayList=new ArrayList<>();
                        JSONArray array =new JSONArray(response.body().string());
                        for(int i =0;i<array.length();i++){
                            JSONObject data =array.getJSONObject(i);
                            String title =data.getString("title");
                            String url =data.getString("url");
                            String thumbnailUrl =data.getString("thumbnailUrl");

                            ImageBean bean=new ImageBean();
                            bean.title=title;
                            bean.url=url;
                            bean.thumbnailUrl=thumbnailUrl;

                            arrayList.add(bean);

                        }

                        iMainPresenter.showImage(arrayList);
                    }else{
                        iMainPresenter.showToast("No image");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    iMainPresenter.showToast(e.toString());
                }finally {
                    iMainPresenter.closeProgress();
                }


            }
        });

    }
}
