package com.denny.apisample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.denny.apisample.contract.MainContract;
import com.denny.apisample.model.MainModel;
import com.denny.apisample.presenter.MainPresenter;
import com.denny.apisample.retrofit.response.InvInfo;
import com.denny.apisample.retrofit.response.ProductInfo;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainContract.IMainView, EasyPermissions.PermissionCallbacks {
    private MainPresenter presenter;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        presenter = new MainPresenter(this);
        MainModel.context = this;
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_get2).setOnClickListener(this);
        findViewById(R.id.btn_get3).setOnClickListener(this);
        findViewById(R.id.btn_get4).setOnClickListener(this);
        findViewById(R.id.btn_post_file_1).setOnClickListener(this);
        findViewById(R.id.btn_post_file_2).setOnClickListener(this);

        checkPremission();
    }

    //region Android 6.0以上取得權限
    public boolean checkPremission() {
        //取得權限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            EasyPermissions.requestPermissions(MainActivity.this, "請按確定並允許儲存權限",
                    100, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        Toast.makeText(getApplicationContext(), "權限拒絕! 無法使用匯出功能", Toast.LENGTH_LONG).show();
    }

    //endregion
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                //使用 OkHttp + Retrofit
                presenter.getInfo_Product();
                break;
            case R.id.btn_get2:
                //使用 OkHttp + Retrofit
                presenter.getInfo_Inv();
                break;
            case R.id.btn_get3:
                //只使用 OkHttp
                presenter.getInfo_ProductByOkhttp();
                break;
            case R.id.btn_get4:
                //只使用 OkHttp
                presenter.getInfo_InvByOkhttp();
                break;
            case R.id.btn_post_file_1:
                //使用 OkHttp + Retrofit ((( JSON + 檔案
                if (!checkPremission()) {
                    return;
                }
                presenter.uploadByRetrofit();
                break;
            case R.id.btn_post_file_2:
                //只使用 OkHttp          ((( JSON + 檔案
                if (!checkPremission()) {
                    return;
                }
                presenter.uploadByOkhttp();
                break;
        }
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("取得資料中...");
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void closeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showErrMsg(final boolean needFinish, final int icon, final String title, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                    dialog = new AlertDialog.Builder(MainActivity.this)
                            .setIcon(icon)
                            .setTitle(title)
                            .setMessage(msg)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (needFinish) {
                                        finish();
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public void showProductInfo(final ProductInfo info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append("編號:");
                    builder.append(info.No);
                    builder.append("\n");
                    builder.append("名稱:");
                    builder.append(info.Name);
                    builder.append("\n");
                    builder.append("單位:");
                    builder.append(info.Unit);
                    builder.append("\n");
                    builder.append("批號管理:");
                    builder.append(info.LotManager);
                    builder.append("\n");
                    dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Tip")
                            .setMessage(builder.toString())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public void showInvInfo(final InvInfo info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append("編號:");
                    builder.append(info.No);
                    builder.append("\n");
                    builder.append("名稱:");
                    builder.append(info.Name);
                    builder.append("\n");
                    builder.append("性質:");
                    builder.append(info.Nature);
                    builder.append("\n");
                    dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Tip")
                            .setMessage(builder.toString())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public void showUploadMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }

                    dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Tip")
                            .setMessage(msg)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }
        });
    }
}
