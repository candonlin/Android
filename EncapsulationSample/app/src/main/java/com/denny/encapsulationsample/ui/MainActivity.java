package com.denny.encapsulationsample.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.denny.encapsulationsample.R;
import com.denny.encapsulationsample.contract.MainContract;
import com.denny.encapsulationsample.presenter.MainPresenter;

/**
 * 主要對於有多種可替代解決方案的業務邏輯，提供一種快速更換的思路
 * 工廠模式
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainContract.IMainView {
    private MainContract.IMainPresenter iMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        iMainPresenter=new MainPresenter(this);
        findViewById(R.id.btn_get).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                iMainPresenter.GetData(this);
                break;
        }
    }
}
