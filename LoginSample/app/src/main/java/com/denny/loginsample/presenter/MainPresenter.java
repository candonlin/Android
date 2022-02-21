package com.denny.loginsample.presenter;

import com.denny.loginsample.contract.MainContract;
import com.denny.loginsample.model.MainModel;

/**
 * author: Denny
 * created on: 2021/2/2 上午 10:40
 */
public class MainPresenter implements MainContract.IMainPresenter {

    private MainContract.IMainView iMainView;
    private MainContract.IMainModel iMainModel;

    public MainPresenter(MainContract.IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModel(this);
    }

    /**
     * request check admin and password
     * @param a admin
     * @param p password
     */
    public void checkInfo(String a, String p) {
        if(a==null || a.isEmpty()){
            iMainView.loginFail(1,"admin can not empty");
            return;
        }

        if(p==null || p.isEmpty()){
            iMainView.loginFail(2,"password can not empty");
            return;
        }
        iMainView.showProgress();
        iMainModel.checkInfo(a,p);
    }

    @Override
    public void closeProgress() {
        iMainView.closeProgress();
    }

    @Override
    public void checkResult(boolean pass, int which, String msg) {
        if(pass){
            iMainView.loginSuccess(msg);
        }else{
            iMainView.loginFail(which,msg);
        }
    }

    @Override
    public void showErrMsg(boolean needFinish, int icon, String title, String msg) {
        iMainView.showErrMsg(needFinish,icon,title,msg);
    }
}
