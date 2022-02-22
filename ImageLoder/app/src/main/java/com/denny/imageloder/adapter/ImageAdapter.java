package com.denny.imageloder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denny.imageloder.ImageUtils.MyBitmapUtils;
import com.denny.imageloder.R;
import com.denny.imageloder.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Denny
 * created on: 2022/2/22 上午 09:44
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<ImageBean> pictureList = new ArrayList<>();
    private OnItemClickListener listener;

    private MyBitmapUtils utils;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
        utils = new MyBitmapUtils(context);
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        final ImageBean picture = pictureList.get(position);

        holder.img.setImageBitmap(null);
        holder.img.setTag(picture.thumbnailUrl);


        holder.tv_title.setText(picture.title);
        utils.show(holder.img, picture.thumbnailUrl);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title;
        private ImageView img;

        public ImageViewHolder(View b) {
            super(b);
            tv_title = b.findViewById(R.id.tv_title);
            img = b.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(pictureList.get(getPosition()));
        }
    }

    public void setData(ArrayList<ImageBean> pictureList) {
        this.pictureList = pictureList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(ImageBean pic);
    }
}
