package com.denny.imageloder.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denny.imageloder.R;
import com.denny.imageloder.adapter.ImageAdapter;
import com.denny.imageloder.bean.ImageBean;
import com.denny.imageloder.contract.MainContract;
import com.denny.imageloder.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView {
    private RecyclerView recyclerView;
    private MainPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init() {
        presenter = new MainPresenter(this);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ImageAdapter adapter = new ImageAdapter(getApplicationContext());

        recyclerView.setAdapter(adapter);

        showProgress();
        presenter.getImage();
    }

    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        });
    }


    @Override
    public void showImage(ArrayList<ImageBean> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (recyclerView.getAdapter() != null) {
                        ((ImageAdapter) recyclerView.getAdapter()).setData(list);
                    }
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
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
