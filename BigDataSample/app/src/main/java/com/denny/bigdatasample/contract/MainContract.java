package com.denny.bigdatasample.contract;

import com.denny.bigdatasample.presenter.MainPresenter;

/**
 * author: Denny
 * created on: 2021/2/2 下午 05:39
 */
public interface MainContract {
    interface IMainModel {
        void getMovies(MainPresenter.ToType type);

        void deleteDataToContentProvider();
    }

    interface IMainView {
        void showProgress();

        void closeProgress();

        void showErrMsg(boolean needFinish, int icon, String title, String msg);

        void showSaveSuccess(int count);

        void showSaveSuccess2(int total, int count);

    }

    interface IMainPresenter {
        void showProgress();

        void closeProgress();

        void showErrMsg(boolean needFinish, int icon, String title, String msg);

        void showSaveSuccess(int count);

        void showSaveSuccess2(int total, int count);
    }
}
