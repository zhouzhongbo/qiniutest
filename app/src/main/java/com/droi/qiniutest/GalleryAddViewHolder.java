package com.droi.qiniutest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouzhongbo on 2017/4/25.
 */

public class GalleryAddViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.img_des) ImageView image;

    public GalleryAddViewHolder(View view){
        super(view);
        ButterKnife.bind(this, view);
    }

    public void fillData(){

    }
}
