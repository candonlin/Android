package com.denny.encapsulationsample.contract;

import android.content.Context;

/**
 * author: Denny
 * created on: 2021/4/16 下午 01:54
 */
public interface MainContract {
    interface IMainModel {
        void GetData(Context context);
    }

    interface IMainView {
    }

    interface IMainPresenter {
        void GetData(Context context);
    }
}
