package com.denny.imageloder.presenter;

import com.denny.imageloder.bean.ImageBean;
import com.denny.imageloder.contract.MainContract;
import com.denny.imageloder.model.MainModel;

import java.util.ArrayList;

/**
 * author: Denny
 * created on: 2022/2/22 上午 10:12
 */
public class MainPresenter implements MainContract.IMainPresenter {
    private MainContract.IMainView iMainView;
    private MainContract.IMainModel iMainModel;

    public MainPresenter(MainContract.IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModel(this);
    }

    @Override
    public void showToast(String msg) {
        iMainView.showToast(msg);
    }

    @Override
    public void getImage() {
        iMainModel.getImage();
    }

    @Override
    public void showImage(ArrayList<ImageBean> list) {
        iMainView.showImage(list);
    }

    @Override
    public void closeProgress() {
        iMainView.closeProgress();
    }
}
