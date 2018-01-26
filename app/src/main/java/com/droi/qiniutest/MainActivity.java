package com.droi.qiniutest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String uptoken = "Ge-MQkJjQPFI91tFaNhp7AgjJxjMhew-NWKBeL3A:dmBiWRZ0ssDAo2tqXKAXiOuVnGE=:eyJzY29wZSI6Inp6Yi1maXJzdC10ZXN0IiwiZGVhZGxpbmUiOjE1MTY4MDY2ODB9";
    private Button btnUpload;
    private TextView textView;
    private UploadManager uploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result_text);
        btnUpload = (Button) findViewById(R.id.button);
        btnUpload.setOnClickListener(this);
        //new一个uploadManager类
        uploadManager = new UploadManager();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.button){
            Log.i("qiniutest", "starting......");
            byte[] data=new byte[]{ 0, 1, 2, 3};
            //设置上传后文件的key
            String upkey = "uploadtest.txt";
            uploadManager.put(data, upkey, uptoken, new UpCompletionHandler() {
                public void complete(String key, ResponseInfo rinfo, JSONObject response) {

                    if(rinfo.isOK()){

                    }else{
                        Log.d("zzb","upload file failed! error ="+rinfo.toString());
                    }

                    btnUpload.setVisibility(View.INVISIBLE);
                    String s = key + ", " + rinfo + ", " + response;
                    Log.i("qiniutest", s);
                    textView.setTextSize(10);
                    String o = textView.getText() + "\r\n\r\n";
                    //显示上传后文件的url
                    textView.setText(o + s + "\n" + "http://xm540.com1.z0.glb.clouddn.com/" + key);
                }
            }, new UploadOptions(null, "test-type", true, null, null));
        }else if(id == R.id.go_test){
            Intent mintent = new Intent(this,TestActivity.class);
            mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mintent);
        }
    }

}
