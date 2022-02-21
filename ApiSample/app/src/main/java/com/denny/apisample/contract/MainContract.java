package com.denny.apisample.contract;

import com.denny.apisample.retrofit.response.InvInfo;
import com.denny.apisample.retrofit.response.ProductInfo;

/**
 * author: Denny
 * created on: 2021/2/3 下午 02:23
 */
public interface MainContract {
    interface IMainModel {
        void getInfo_Product();

        void getInfo_Inv();

        void getInfo_ProductByOkhttp();

        void getInfo_InvByOkhttp();

        void uploadByRetrofit();

        void uploadByOkhttp();

    }

    interface IMainView {
        void showProgress();

        void closeProgress();

        void showErrMsg(boolean needFinish, int icon, String title, String msg);

        void showProductInfo(ProductInfo info);

        void showInvInfo(InvInfo info);

        void showUploadMessage(String msg);


    }

    interface IMainPresenter {
        void showProgress();

        void closeProgress();

        void showErrMsg(boolean needFinish, int icon, String title, String msg);

        void showProductInfo(ProductInfo info);

        void showInvInfo(InvInfo info);

        void showUploadMessage(String msg);

    }
}
