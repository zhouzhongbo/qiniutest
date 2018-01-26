package com.droi.qiniutest;

import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;

/**
 * Created by zhouzhongbo on 2018/1/24.
 */

public class QiniuHelper {

    public static QiniuHelper qiniuHelper;
    Configuration config;
    private String  upToken;
    private UploadManager uploadManager;

    private QiniuHelper(){
//        Recorder recorder = new Recorder() {
//            @Override
//            public void set(String key, byte[] data) {
//
//            }
//
//            @Override
//            public byte[] get(String key) {
//                return new byte[0];
//            }
//
//            @Override
//            public void del(String key) {
//
//            }
//        }
        config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

//                .recorder()
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
        if(upToken == null){
            upToken = "Ge-MQkJjQPFI91tFaNhp7AgjJxjMhew-NWKBeL3A:dmBiWRZ0ssDAo2tqXKAXiOuVnGE=:eyJzY29wZSI6Inp6Yi1maXJzdC10ZXN0IiwiZGVhZGxpbmUiOjE1MTY4MDY2ODB9";
            //here may need api to get token from server;
        }


    }

    public static QiniuHelper getInstance(){
        if(qiniuHelper == null){
            qiniuHelper = new QiniuHelper();
        }
        return qiniuHelper;
    }

    /**
     * upload byte[] data
     * @param data
     * @param key
     * @param complete
     * @param options
     */
    public void upLoadFile(byte[] data,String key,final UpCompletionHandler complete, final UploadOptions options) {
        if (uploadManager == null) {
            Log.d("zzb", "init failed!");
            return;
        }
        uploadManager.put(data, key, upToken, complete, options);
    }

    /**
     * update file from filepath
     * @param filePath
     * @param key
     * @param completionHandler
     * @param options
     */
    public void upLoadFile(String filePath,String key,final UpCompletionHandler completionHandler, final UploadOptions options){
        if (uploadManager == null) {
            Log.d("zzb", "init failed!");
            return;
        }
        uploadManager.put(filePath, key, upToken, completionHandler, options);
    }

    /**
     * upload file from File
     * @param file
     * @param key
     * @param completionHandler
     * @param options
     */
    public void upLoadFile(File file,String key,final UpCompletionHandler completionHandler, final UploadOptions options){
        if (uploadManager == null) {
            Log.d("zzb", "init failed!");
            return;
        }
        uploadManager.put(file, key, upToken, completionHandler, options);
    }








}
