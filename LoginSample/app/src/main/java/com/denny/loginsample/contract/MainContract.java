package com.denny.loginsample.contract;

/**
 * author: Denny
 * created on: 2021/2/2 上午 10:40
 */
public interface MainContract {
    interface IMainModel {
        /**
         * 驗證帳密
         *
         * @param a
         * @param p
         */
        void checkInfo(String a, String p);

    }

    interface IMainView {
        void showProgress();

        void closeProgress();

        void showErrMsg(boolean needFinish, int icon, String title, String msg);

        void loginSuccess(String msg);

        /**
         *
         * @param which 1:帳號錯誤 ; 2:密碼錯誤
         * @param msg 錯誤訊息
         */
        void loginFail(int which, String msg);
    }

    interface IMainPresenter {
        void closeProgress();

        void checkResult(boolean pass, int which, String msg);

        void showErrMsg(boolean needFinish, int icon, String title, String msg);
    }
}
