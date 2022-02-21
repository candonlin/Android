package com.denny.studysample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;

import com.denny.studysample.http.HttpsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        OkHttpClient

        init();
    }

    private void init() {
        findViewById(R.id.btn_https).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_https:
                Intent https_page = new Intent(MainActivity.this, HttpsActivity.class);
                startActivity(https_page);
                break;
        }
    }
}