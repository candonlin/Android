package com.denny.bigdatasample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.denny.bigdatasample.contract.MainContract;
import com.denny.bigdatasample.db.AppDataBase;
import com.denny.bigdatasample.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView, View.OnClickListener {
    private MainPresenter presenter;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        presenter = new MainPresenter(this, this);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);

        findViewById(R.id.btn_get2).setOnClickListener(this);
        findViewById(R.id.btn_delete2).setOnClickListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                presenter.getMovies(MainPresenter.ToType.SELF);
                break;
            case R.id.btn_delete:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDataBase.getInstance(MainActivity.this).movieDAO().Delete();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "刪除成功", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).start();
                break;


            case R.id.btn_get2:
                if (presenter.isInstallAPP(MainActivity.this, "com.regalscan.contentprovidersample")) {
                    presenter.getMovies(MainPresenter.ToType.CONTENTPROVIDER);

                } else {
                    Toast.makeText(MainActivity.this, "資料庫程式尚未安裝", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete2:
                if (presenter.isInstallAPP(MainActivity.this, "com.regalscan.contentprovidersample")) {
                    presenter.deleteDataToContentProvider();
                    Toast.makeText(MainActivity.this, "刪除成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "資料庫程式尚未安裝", Toast.LENGTH_SHORT).show();

                }
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
                    progressDialog.setTitle("資料下載中...");
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
    public void showSaveSuccess(final int count) {
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
                            .setTitle("Success")
                            .setMessage("儲存筆數：" + count)
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
    public void showSaveSuccess2(final int total, final int count) {
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
                            .setTitle("Success")
                            .setMessage("共" + total + "筆\n已存筆數：" + count)
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
