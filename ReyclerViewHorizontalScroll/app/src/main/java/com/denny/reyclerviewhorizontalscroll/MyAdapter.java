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
public class MyAdapter extends RecyclerView.Adapter {
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;

    ArrayList<DataBean> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false);
            return new VHHeaderTitle(view);

        } else if (viewType == TYPE_ITEMS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_body, parent, false);

            return new VHItems(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItems) {
            VHItems vhItems = (VHItems) holder;
            DataBean bean = arrayList.get(position - 1);
            vhItems.tv1.setText("" + bean.getS1());
            vhItems.tv2.setText(bean.getS2());
            vhItems.tv3.setText(bean.getS3());
            vhItems.tv4.setText(bean.getS4());
            vhItems.tv5.setText(bean.getS5());
            vhItems.tv6.setText(bean.getS6());

        }
    }

    protected void clear() {
        arrayList.clear();
        notifyDataSetChanged();

    }

    public void setNewData(ArrayList<DataBean> arrayList) {
        clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                return arrayList.size() + 1;
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEMS;

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private class VHHeaderTitle extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button f1;
        public VHHeaderTitle(View view) {
            super(view);
            f1=view.findViewById(R.id.f1);
            f1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.f1:
                    Collections.sort(arrayList, new Comparator<DataBean>() {
                        @Override
                        public int compare(DataBean lhs, DataBean rhs) {
                            return rhs.getS1()-(lhs.getS1());
                        }
                    });

//                    Collections.sort(arrayList, new FishNameComparator());
                    notifyDataSetChanged();
                    break;
            }
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

//    public class FishNameComparator implements Comparator<DataBean>
//    {
//        public int compare(DataBean left, DataBean right) {
//            return left.getS1().compareTo(right.getS1());
//        }
//    }
}
