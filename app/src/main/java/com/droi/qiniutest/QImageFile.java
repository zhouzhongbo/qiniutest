package com.droi.qiniutest;

import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;

/**
 * Created by zhouzhongbo on 2018/1/26.
 */

public class QImageFile extends File {

    String qUrl;
    public QImageFile(@NonNull String pathname) {
        super(pathname);
    }

    public QImageFile(String parent, @NonNull String child) {
        super(parent, child);
    }

    public QImageFile(File parent, @NonNull String child) {
        super(parent, child);
    }

    public QImageFile(@NonNull URI uri) {
        super(uri);
    }

    public void setQurl(String url){
        qUrl = url;
    }
}
