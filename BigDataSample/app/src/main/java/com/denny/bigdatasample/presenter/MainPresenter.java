package com.denny.bigdatasample.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.denny.bigdatasample.contract.MainContract;
import com.denny.bigdatasample.model.MainModel;

/**
 * author: Denny
 * created on: 2021/2/2 下午 05:39
 */
public class MainPresenter implements MainContract.IMainPresenter {
    private MainContract.IMainView iMainView;
    private MainContract.IMainModel iMainModel;

    public MainPresenter(Context context, MainContract.IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModel(context,this);
    }

    public static enum  ToType{
        SELF,CONTENTPROVIDER
    }
    public void getMovies(ToType type){
        iMainModel.getMovies(type);
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
        iMainView.showErrMsg(needFinish,icon,title,msg);
    }

    @Override
    public void showSaveSuccess(int count) {
        iMainView.showSaveSuccess(count);
    }

    @Override
    public void showSaveSuccess2(int total, int count) {
        iMainView.showSaveSuccess2(total,count);
    }


    //判斷程式是否安裝
    public  boolean isInstallAPP(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public  void deleteDataToContentProvider(){
        iMainModel.deleteDataToContentProvider();
    }
}
