package com.denny.loginsample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.denny.loginsample.contract.MainContract;
import com.denny.loginsample.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView, View.OnClickListener {

    private MainPresenter presenter;
    private TextInputEditText edit_admin;
    private TextInputEditText edit_pass;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        presenter = new MainPresenter(this);
        edit_admin = findViewById(R.id.edit_admin);
        edit_pass = findViewById(R.id.edit_pass);
        findViewById(R.id.btn_login).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                presenter.checkInfo(edit_admin.getText().toString().trim(), edit_pass.getText().toString().trim());
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
                    progressDialog.setTitle("驗證中...");
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
    public void loginSuccess(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void loginFail(final int which, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    switch (which) {
                        case 1:
                            edit_admin.setError(msg);
                            break;
                        case 2:
                            edit_pass.setError(msg);
                            break;
                    }
                }
            }
        });
    }
}
