package com.denny.encapsulationsample.presenter;

import android.content.Context;

import com.denny.encapsulationsample.contract.MainContract;
import com.denny.encapsulationsample.model.MainModel;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:54
 */
public class MainPresenter implements MainContract.IMainPresenter {
    private MainContract.IMainView iMainView;
    private MainContract.IMainModel iMainModel;

    public MainPresenter(MainContract.IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModel();
    }

    @Override
    public void GetData(Context context) {
        iMainModel.GetData(context);
    }
}
