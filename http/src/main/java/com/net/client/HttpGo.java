package com.net.client;

import android.app.Application;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.net.client.config.HttpContants;

import okhttp3.Interceptor;

/**
 * 网络请求端，可进行post get请求，文件下载操作.
 * Created by xiepc on 2016/11/14.
 */
public class HttpGo {


    private static HttpGo instance;
    /**网络请求超时时间*/
    private static int TIME_OUT = 20000;


    public static void init(Application app) {
        if (instance == null) {
            instance = new HttpGo();
        }
        OkGo.init(app);
        try {
            if(true){
                //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
               OkGo.getInstance().debug("OkGo");
            }
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
//                    .addInterceptor(new HttpRequestInterceptor())
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(TIME_OUT)  //全局的连接超时时间
                    .setReadTimeOut(TIME_OUT)     //全局的读取超时时间
                    .setWriteTimeOut(TIME_OUT)    //全局的写入超时时间
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy0216/
                    .setCacheMode(CacheMode.NO_CACHE)
                    .setRetryCount(1)   //让它超时重发1次请求
                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addInterceptor(Interceptor  interceptor){
        OkGo.getInstance().addInterceptor(interceptor);
    }

    /**
     * 添加全局，公共参数
     * @param params
     */
    public static void addCommonParams(HttpParams params){
        OkGo.getInstance().addCommonParams(params);
    }   //设置全局公共参数

    public static HttpRequest post(String url) {
        Log.i(HttpContants.LOG_TAG,"url----" + url);
        return new HttpRequest(url,HttpRequest.REQUEST_POST);
    }
    public static HttpRequest get(String url) {
        Log.i(HttpContants.LOG_TAG,"url----" + url);
        return new HttpRequest(url,HttpRequest.REQUEST_GET);
    }

    public static void cancelByTag(Object obj) {
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(obj);
    }

    public static void cancelAll() {
        Log.d(HttpContants.LOG_TAG,"取消所有网络请求");
        //取消所有请求
        OkGo.getInstance().cancelAll();
    }

}
