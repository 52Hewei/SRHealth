package com.net.client;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.net.client.config.HttpContants;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;

/**
 * Created by xiepc on 2016/12/29 20:38
 */

public class HttpRequest {

    private BaseRequest request;
    private OkGo go;
    private HttpRequest instance;
    String url;
    /**
     * post请求
     */
    public static final int REQUEST_POST = 10;
    /**
     * get请求
     */
    public static final int REQUEST_GET = 11;

    public HttpRequest(String url , int requestMode) {
        this.url = url;
        go = OkGo.getInstance();
        instance = this;
        if(requestMode == REQUEST_POST){
            request = go.post(url);
        }else if(requestMode == REQUEST_GET){
            request = go.get(url);
        }
    }

    public HttpRequest tag(Object obj) {
        request.tag(obj);
        return instance;
    }

    public HttpRequest params(String key, String value) {
        Log.i(HttpContants.LOG_TAG,key + "----" + value);
        request.params(key, value);
        return instance;
    }
    public HttpRequest params(String key, File file) {
        Log.i(HttpContants.LOG_TAG,key + "----" + file.getAbsolutePath());
        if(request instanceof PostRequest){
            ((PostRequest)request).params(key, file);
        }else if(request instanceof GetRequest){
            Log.d(HttpContants.LOG_TAG,"GET请求不支持上传文件");
            return null;
        }
        return instance;
    }

    public HttpRequest json(String jsonStr) {
        if (request instanceof PostRequest) {
            ((PostRequest) request).upJson(jsonStr);
        }
        return instance;
    }

    public HttpRequest headers(String key, String value) {
        Log.i(HttpContants.LOG_TAG,"headers--" + key + "----" + value);
        request.headers(key, value);
        return instance;
    }

    public void execute(BaseListener listener) {
//        if (!isNetworkConnected(OkGo.getContext())) {
//            Toast.makeText(OkGo.getContext(), "无网络状态", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (listener instanceof HttpFileListener) {
            HttpFileListener httpFileListener = (HttpFileListener) listener;
            if (!TextUtils.isEmpty(httpFileListener.getDestFileDir())) { //如果文件保存路径存在
                request.execute(new ConvertFileCallback(httpFileListener, httpFileListener.getDestFileDir(), httpFileListener.getDestFileName())); //下载文件回调监听
            } else {
                //如果只传了保存文件的名称，默认下载到  Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator+destFileName
                request.execute(new ConvertFileCallback(httpFileListener, httpFileListener.getDestFileName())); //下载文件回调监听
            }
        } else {
            request.execute(new ConvertStringCallback(listener)); //普通字符串请求回调
        }
    }

    /**
     * 同步请求
     *
     * @return
     */
    public String execute() {
        try {
            Response response = request.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void cancel() {
        go.cancelTag(this.toString());
    }

}
