package com.net.client;

import okhttp3.Response;

/**
 * 文件上传回调监听
 * Created by xiepc on 2017/2/27 17:58
 */

public abstract class HttpUploadFileListener extends ABaseListener<String, Response, Exception> {

    public abstract void onProgress(long currentSize, long totalSize, float progress, long networkSpeed);
    public abstract void onFinish();

}
