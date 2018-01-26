package com.droi.qiniutest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by zhouzhongbo on 2018/1/26.
 */

public class CameraHelper {
    Activity myactivity;
    // 拍照成功，读取相册成功，裁减成功
    private final int  ALBUM_OK = 1, CAMERA_OK = 2,CUT_OK = 3;

    public CameraHelper(Activity mactivity){
        myactivity = mactivity;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case CAMERA_OK:
//                if (data != null) {
//                    clipPhoto(fileurl,CAMERA_OK);//开始裁减图片
//                }else{
//                    Log.d("zzb","data is null!");
//                }
//                break;
//            case ALBUM_OK:
//                Log.d("zzb","onactivityresult:ALBUM_OK");
//
//                if (data != null) {
//                    Log.d("zzb","onactivityresult:ALBUM_OK:data != null");
//                    setPicToView(data);
//                }else {
//                    Log.e("zzb","data为空");
//                }
//                break;
//            case CUT_OK:
//                if (data != null) {
//                    Log.d("zzb","data ="+data.toString());
//                    setPicToView(data);
//                }else {
//                    Log.e("zzb","data为空");
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            useCamera();
//        } else {
//            // 没有获取 到权限，从新请求，或者关闭app
//            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//    public void applyWritePermission() {
//
//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
//            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//            if (check == PackageManager.PERMISSION_GRANTED) {
//                //调用相机
//                useCamera();
//            } else {
//                myactivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        } else {
//            useCamera();
//        }
//    }
//
//
//    private void useCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
//        String name= System.currentTimeMillis() + ".jpg";
//        Log.d("zzb","name="+name);
//        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/images/" + System.currentTimeMillis() + ".jpg");
//        if(!file.getParentFile().exists()){
//            file.getParentFile().mkdir();
//        }
//        if(!file.exists()){
//            Log.d("zzb","file is not exist!!");
//        }
//        fileurl  = FileProvider.getUriForFile(this, "com.droi.qiniutest.fileprovider", file);
//        //添加权限
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileurl);
//        startActivityForResult(intent, CAMERA_OK);
//    }
//
//    /**
//     * 裁剪图片方法实现
//     * @param uri          图片uri
//     * @param type         类别：相机，相册
//     */
//    public void clipPhoto(Uri uri, int type) {
//        Log.d("zzb","clipPhoto!!!!");
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 500);
//        intent.putExtra("outputY", 500);
//        intent.putExtra("return-data", false);
//
//        /**
//         * 此处做一个判断
//         * １，相机取到的照片，我们把它做放到了定义的目录下。就是file
//         * ２，相册取到的照片，这里注意了，因为相册照片本身有一个位置，我们进行了裁剪后，要给一个裁剪后的位置，
//         * 　　不然onActivityResult方法中，data一直是null
//         */
//        if(type==CAMERA_OK) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }else {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    FileProvider.getUriForFile(TestActivity.this,
//                            "com.droi.qiniutest.fileprovider",
//                            file));
//        }
//        startActivityForResult(intent, CUT_OK);
//    }
//
//    /**
//     * 保存裁剪之后的图片数据 将图片设置到imageview中
//     *
//     * @param picdata 资源
//     */
//    private void setPicToView(Intent picdata) {
//
//        try
//        {
//            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picdata.getData()));
//            imgView.setImageBitmap(bitmap);
//        }catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
}
