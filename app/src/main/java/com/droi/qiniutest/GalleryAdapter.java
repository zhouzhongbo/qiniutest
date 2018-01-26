package com.droi.qiniutest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzhongbo on 2017/4/24.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    QiniuHelper qiniuHelper;
    private ArrayList<File> desImg = new ArrayList<File>();
    private Context context;
    private LayoutInflater mInflater;
    private static final int TYPE_IMG_CONTENT = 0;
    private static final int TYPE_IMG_ADD = 1;
    private OnItemClickListener itemClickListener;

    public GalleryAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        qiniuHelper = QiniuHelper.getInstance();
    }

    @Override
    public int getItemCount() {
        return desImg.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_IMG_ADD;
        } else {
            return TYPE_IMG_CONTENT;
        }
    }

    public void addImageData(ArrayList<File> object){
        desImg.addAll(object);
    }

    public void saveImgInBackground(ArrayList<File> object,int position){
//        itemholder.pb.setMax(100);
        File img = object.get(position);
        qiniuHelper.upLoadFile(img,img.getName(),
                new UpCompletionHandler() {
                    public void complete(String key, ResponseInfo rinfo, JSONObject response) {
                        if(rinfo.isOK()){
                            String url="http://p3034n2on.bkt.clouddn.com/"+key;
                            Log.d("zzb","file url is : "+url);
//                            itemholder.pb.setVisibility(View.GONE);
                        }else{
                            Log.d("zzb","upload file failed! error ="+rinfo.toString());
                        }
                    }
                },
                new UploadOptions(null, null, false,
                        new UpProgressHandler(){
                            public void progress(String key, double percent){
                                Log.i("qiniu", key + ": " + percent);
//                                itemholder.pb.setProgress(progress);
                            }
                        }, null)
        );
    }

    public ArrayList<File> getImgList(){
        return desImg;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_IMG_ADD) {
            View view = mInflater.inflate(R.layout.gallery_add_layout, viewGroup, false);
            return new GalleryAddViewHolder(view);
        } else if (viewType == TYPE_IMG_CONTENT) {
            View view = mInflater.inflate(R.layout.gallery_item_layout, viewGroup, false);
            return  new GalleryItemViewHolder(view);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_IMG_ADD) {
            GalleryAddViewHolder addholder = (GalleryAddViewHolder)holder;
            addholder.image.setImageResource(R.mipmap.img_add);
            //为整体布局设置点击事件，触发接口的回调
            addholder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null)
                        itemClickListener.onAddImageClick(desImg);
                }
            });
        } else if (type == TYPE_IMG_CONTENT) {
            final GalleryItemViewHolder itemholder = (GalleryItemViewHolder)holder;
            File  img = desImg.get(position);
//            if(img.hasUri()){
//                Glide.with(context)
//                        .load(img.getUri())
//                        .placeholder(R.drawable.img_waiting)
//                        .error(R.drawable.img_waiting)
//                        .into(itemholder.image);
//                itemholder.image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(itemClickListener!=null)
//                            itemClickListener.onPreViewImageClick(desImg,position);
//                    }
//                });
//                itemholder.remove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(itemClickListener!=null)
//                            itemClickListener.onRemoveImageClick(desImg,position);
//                    }
//                });
//                itemholder.pb.setVisibility(View.GONE);
//            }else
            {
                itemholder.pb.setVisibility(View.GONE);
                Glide.with(context)
                        .load(img)
                        .placeholder(R.mipmap.img_waiting)
                        .error(R.mipmap.img_waiting)
                        .into(itemholder.image);
                itemholder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("zzb","image on clicked!");
                        if(itemClickListener!=null)
                            itemClickListener.onPreViewImageClick(desImg,position);
                    }
                });
                itemholder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("zzb","remove on clicked!");
                        if(itemClickListener!=null)
                            itemClickListener.onRemoveImageClick(desImg,position);
                    }
                });
            }
        }
    }


    //点击事件接口
    public interface OnItemClickListener{
        void onAddImageClick(List<File> desImg);
        void onRemoveImageClick(List<File> desImg, int position);
        void onPreViewImageClick(List<File> desImg, int position);
    }
    //设置点击事件的方法
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}