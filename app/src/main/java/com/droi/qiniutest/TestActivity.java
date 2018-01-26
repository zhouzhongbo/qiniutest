package com.droi.qiniutest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends Activity implements GalleryAdapter.OnItemClickListener, TakePhoto.TakeResultListener,InvokeListener {
    TakePhoto takePhoto;
    InvokeParam invokeParam;
    PopupWindow mPopupWindow;
    GalleryAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.gallery_list)
    RecyclerView mylist;

    @BindView(R.id.test_button)
    Button testButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.test_button)
    void testButtonClick(){
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
    }


    private void init(){
        //toolbar init
        toolbar.setTitle(R.string.toolbar_back);
        toolbar.setNavigationIcon(R.mipmap.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //recyclelist init
        adapter = new GalleryAdapter(this);
        mylist.setAdapter(adapter);
        adapter.setItemClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mylist.setLayoutManager(linearLayoutManager);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    @Override
    public void onAddImageClick(List<File> desImg) {
        ShowDialog();
    }

    @Override
    public void onRemoveImageClick(List<File> desImg, int position) {
        desImg.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPreViewImageClick(List<File> desImg, int position) {
        File  file = desImg.get(position);
//        if(!file.hasUri()) {
//            Toast.makeText(this,R.string.file_wait_for_saving,Toast.LENGTH_SHORT).show();
//            return;
//        }
        View popupView = getLayoutInflater().inflate(R.layout.popopwin_preview_img_layout, null);
        popupView.findViewById(R.id.close_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        ImageView preview = (ImageView) popupView.findViewById(R.id.img_preview);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        Glide.with(this)
                .load(file)
                .into(preview);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_test, null);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER,0,0);
    }


    CropOptions op =  new CropOptions.Builder().
            setAspectX(200).setAspectY(200).
            setWithOwnCrop(true).
            create();

    private void ShowDialog(){
        new AlertDialog.Builder(this).setItems(
                new String[] { "拍摄照片", "从相册选择"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto.onPickFromCaptureWithCrop(UnitUtil.createOutUri(),op);
                                break;
                            case 1:
                                takePhoto.onPickMultipleWithCrop(10,op);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }


    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> im =  result.getImages();
        ArrayList<File> cache = new ArrayList<File>();
        for(int i=0;i<im.size();i++){
            String imagePath = im.get(i).getOriginalPath();
            Log.d("zzb","o-imagepath="+imagePath);
            Log.d("zzb","c-imagepath="+im.get(i).getCompressPath());
            File img = new File(imagePath);
            cache.add(img);
        }
        adapter.addImageData(cache);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
