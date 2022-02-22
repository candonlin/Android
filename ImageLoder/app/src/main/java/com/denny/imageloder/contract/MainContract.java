package com.denny.imageloder.contract;

import com.denny.imageloder.bean.ImageBean;

import java.util.ArrayList;

/**
 * author: Denny
 * created on: 2022/2/22 上午 10:08
 */
public interface MainContract {

    interface IMainView {
        void showImage(ArrayList<ImageBean> list);

        void closeProgress();

        void showToast(String msg);

    }

    interface IMainPresenter {
        void showToast(String msg);

        void getImage();

        void showImage(ArrayList<ImageBean> list);

        void closeProgress();

    }

    interface IMainModel {

        void getImage();
    }
}