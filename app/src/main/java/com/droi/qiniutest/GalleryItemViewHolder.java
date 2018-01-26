package com.droi.qiniutest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouzhongbo on 2017/4/25.
 */

public class GalleryItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_des) ImageView image;
    @BindView(R.id.remove_button) ImageView remove;
    @BindView(R.id.update_progress) ProgressBar pb;


    public GalleryItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}