package com.denny.reyclerviewhorizontalscroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * author: Denny
 * created on: 2021/10/20 下午 05:40
 */
public class TestAdapter extends RecyclerView.Adapter {
    ArrayList<DataBean> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_body, parent, false);
        return new VHItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItems) {
            VHItems vhItems = (VHItems) holder;
            DataBean bean = arrayList.get(position);
            vhItems.tv1.setText("" + bean.getS1());
            vhItems.tv2.setText(bean.getS2());
            vhItems.tv3.setText(bean.getS3());
            vhItems.tv4.setText(bean.getS4());
            vhItems.tv5.setText(bean.getS5());
            vhItems.tv6.setText(bean.getS6());
        }
    }

    public void setNewData(ArrayList<DataBean> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void sort(int type) {
        switch (type) {
            case 1:
                Collections.sort(arrayList, new Comparator<DataBean>() {
                    @Override
                    public int compare(DataBean lhs, DataBean rhs) {
                        return rhs.getS1()-(lhs.getS1());
                    }
                });
                notifyDataSetChanged();
                break;
            case 2:
                Collections.sort(arrayList, new Comparator<DataBean>() {
                    @Override
                    public int compare(DataBean lhs, DataBean rhs) {
                        return lhs.getS1()-(rhs.getS1());
                    }
                });
                notifyDataSetChanged();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:

                break;
        }
    }


    private class VHItems extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
        public TextView tv5;
        public TextView tv6;

        public VHItems(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv5);
            tv5 = itemView.findViewById(R.id.tv6);
            tv6 = itemView.findViewById(R.id.tv6);
        }
    }


}
