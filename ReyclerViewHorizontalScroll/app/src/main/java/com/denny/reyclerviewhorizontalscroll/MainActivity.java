package com.denny.reyclerviewhorizontalscroll;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter();
        recyclerview.setAdapter(adapter);
        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
        ArrayList<DataBean> arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            DataBean bean = new DataBean();
            bean.setS1(i);
            bean.setS2(""+i);
            bean.setS3("asd");
            bean.setS4("asd");
            bean.setS5("asd");
            bean.setS6("asd");
            arrayList.add(bean);
        }
        adapter.setNewData(arrayList);
    }
}