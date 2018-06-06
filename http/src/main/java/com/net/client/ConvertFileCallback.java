package com.net.client;

import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.BaseRequest;
import com.net.client.config.HttpContants;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 文件下载回调类
 * Created by xiepc on 2016/11/16.
 */

public class ConvertFileCallback extends FileCallback {
    private HttpFileListener listener;
    public ConvertFileCallback(BaseListener listener,String destFileName){
        super(destFileName);
        this.listener = (HttpFileListener)listener;
    }
    public ConvertFileCallback(BaseListener listener, String destFileDir, String destFileName){
        super(destFileDir,destFileName);
        this.listener = (HttpFileListener)listener;
    }

    @Override
    public void onSuccess(File file, Call call, Response response) {
        Log.i(HttpContants.LOG_TAG,"下载成功路径："+ file.getAbsolutePath());
        listener.onSuccess(file);
    }
    @Override
    public void onBefore(BaseRequest request) {
        listener.onStart();
    }

    @Override
    public void onAfter(@Nullable File file, @Nullable Exception e) {
        super.onAfter(file, e);
        listener.onFinish();
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        listener.onProgress(currentSize, totalSize, progress, networkSpeed);
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        Log.i(HttpContants.LOG_TAG,"文件下载错误："+e.getMessage());
        listener.onFailure(response,e);
    }
}
