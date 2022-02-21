package com.denny.reyclerviewhorizontalscroll;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    HorizontalScrollView hor_title, hor_content;
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    private void init() {
        hor_title = findViewById(R.id.hor_title);
        hor_content = findViewById(R.id.hor_content);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        TestAdapter adapter = new TestAdapter();
        ArrayList<DataBean> arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            DataBean bean = new DataBean();
            bean.setS1(i);
            bean.setS2("" + i);
            bean.setS3("asd");
            bean.setS4("asd");
            bean.setS5("asd");
            bean.setS6("asd");
            arrayList.add(bean);
        }
        adapter.setNewData(arrayList);
        recyclerview.setAdapter(adapter);


        hor_title.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                hor_content.scrollTo(scrollX, scrollY);
            }
        });

        hor_content.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY != oldScrollY)
                    return;
                hor_title.scrollTo(scrollX, 0);
            }
        });


        findViewById(R.id.f1).setOnClickListener(this);
        findViewById(R.id.f2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f1:
                if (recyclerview.getAdapter() != null) {
                    ((TestAdapter) recyclerview.getAdapter()).sort(1);
                }
                break;
            case R.id.f2:
                if (recyclerview.getAdapter() != null) {
                    ((TestAdapter) recyclerview.getAdapter()).sort(2);
                }
                break;
            case R.id.f3:
                break;
            case R.id.f4:
                break;
            case R.id.f5:
                break;
            case R.id.f6:
                break;


        }
    }
}