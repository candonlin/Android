package com.denny.apisample.presenter;

import com.denny.apisample.contract.MainContract;
import com.denny.apisample.model.MainModel;
import com.denny.apisample.retrofit.response.InvInfo;
import com.denny.apisample.retrofit.response.ProductInfo;

/**
 * author: Denny
 * created on: 2021/2/3 下午 02:23
 */
public class MainPresenter implements MainContract.IMainPresenter {
    private MainContract.IMainView iMainView;
    private MainContract.IMainModel iMainModel;

    public MainPresenter(MainContract.IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModel(this);
    }

    @Override
    public void showProgress() {
        iMainView.showProgress();
    }

    @Override
    public void closeProgress() {
        iMainView.closeProgress();
    }

    @Override
    public void showErrMsg(boolean needFinish, int icon, String title, String msg) {
        iMainView.showErrMsg(needFinish, icon, title, msg);
    }

    @Override
    public void showProductInfo(ProductInfo info) {
        iMainView.showProductInfo(info);
    }

    @Override
    public void showInvInfo(InvInfo info) {
        iMainView.showInvInfo(info);

    }

    @Override
    public void showUploadMessage(String msg) {
        iMainView.showUploadMessage(msg);
    }

    public void getInfo_Product() {
        iMainModel.getInfo_Product();
    }

    public void getInfo_Inv() {
        iMainModel.getInfo_Inv();
    }

    public void getInfo_ProductByOkhttp() {
        iMainModel.getInfo_ProductByOkhttp();
    }

    public void getInfo_InvByOkhttp() {
        iMainModel.getInfo_InvByOkhttp();
    }


    public void uploadByRetrofit() {
        iMainModel.uploadByRetrofit();
    }

    public void uploadByOkhttp() {
        iMainModel.uploadByOkhttp();
    }
}
