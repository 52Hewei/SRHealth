package com.net.client;

import java.io.File;

import okhttp3.Response;

/**
 * 文件下载监听类
 * Created by xiepc on 2016/11/16.
 */

public abstract class HttpFileListener extends ABaseListener<File, Response,Exception>{
    private String destFileDir;
    private String destFileName;
    public HttpFileListener(String destFileDir, String destFileName){
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }
    public HttpFileListener(String destFileName){
        this.destFileName = destFileName;
    };
    public abstract void onProgress(long currentSize, long totalSize, float progress, long networkSpeed);
    public abstract void onFinish();

    public String getDestFileDir(){
        return destFileDir;
    }

    public String getDestFileName(){
        return destFileName;
    }
}
