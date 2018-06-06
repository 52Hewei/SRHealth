package com.net.client;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.net.client.bean.ResponseData;
import com.net.client.config.HttpContants;
import com.net.client.dialog.LoadingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 字符串回调callback
 * Create by xiepc on 2016/11/15 10:25
 */
public class ConvertStringCallback extends StringCallback {

//    private ProgressDialog dialog;

    private BaseListener listener;

    private LoadingDialog loadingDialog;

    private  Gson gson = new Gson();


    public ConvertStringCallback(BaseListener listener) {
        this.listener = listener;
        if (listener instanceof HttpDialogListener) {
            HttpDialogListener httpDialogListener = (HttpDialogListener) listener;
            loadingDialog = new LoadingDialog();
            if (TextUtils.isEmpty(httpDialogListener.getMsg())) {
                loadingDialog.setMessage("请求网络中...");
            } else {
                loadingDialog.setMessage(httpDialogListener.getMsg());
            }
        }
    }


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);

        //网络请求前显示对话框
//        if (loadingDialog != null && loadingDialog.getDialog() != null && !loadingDialog.getDialog().isShowing()) {
        if (loadingDialog != null) {
            loadingDialog.show(((HttpDialogListener) listener).getActivity().getFragmentManager(), "LoadingDialog");
        }

        if (listener instanceof ABaseListener) {
            ((ABaseListener) listener).onStart();
        }
    }

    @Override
    public void onAfter(@Nullable String s, @Nullable Exception e) {
        super.onAfter(s, e);
        //网络请求结束后关闭对话框
        try { //加个处理防止，当前Activity关闭，网络还在请求导致对话框关闭时异常   
//            if (dialog != null && dialog.isShowing()) {  // TODO: 2017/4/26  待优化逻辑
//                dialog.dismiss();
//            }
            if (loadingDialog != null && loadingDialog.getDialog() != null && loadingDialog.getDialog().isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if(listener instanceof HttpUploadFileListener){
                ((HttpUploadFileListener)listener).onFinish();
            }
        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if(listener instanceof HttpBeanListener){
             String msg = e==null? "未知网络错误":e.getMessage();
            ((HttpBeanListener)listener).onFailure(""+response.code(),msg);
        }else{
            ((ABaseListener)listener).onFailure(response,e);
        }
    }

    @Override
    public void onSuccess(String s, Call call, Response response) {
        Log.i(HttpContants.LOG_TAG,"请求结果:" + s);
        if(listener instanceof HttpBeanListener){
        //        ResponseData<T> responseData = gson.fromJson(s,new TypeToken<ResponseData<T>>(){}.getType());  //此方法由于泛型类型未指定，执行结果失败
//                t = gson.fromJson(gsonString, cls);
//                ((HttpBeanListener)listener).onSuccess(JsonUtil.gsonToBean(s,((HttpBeanListener)listener).getEntityClass()));
//                ((HttpBeanListener)listener).onSuccess((ResponseData) gson.fromJson(s, ((HttpBeanListener)listener).getEntityClass()));
                  Class z = ((HttpBeanListener)listener).getEntityClass();
                  ResponseData responseData = jsonToBean(s,z);
                 if(responseData.getErrCode().equals(HttpContants.CODE_SUCCESS)){
                     ((HttpBeanListener)listener).onSuccess(responseData.getData());
                  }else{
                     ((HttpBeanListener)listener).onFailure(responseData.getErrCode(),responseData.getErrDesc());
                 }

        }else{ //传递字符串
            ((ABaseListener)listener).onSuccess(s);
        }

    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.upProgress(currentSize,totalSize,progress,networkSpeed);
        //这里回调上传进度(该回调在主线程,可以直接更新ui)
        if(listener instanceof HttpUploadFileListener){
            ((HttpUploadFileListener)listener).onProgress(currentSize, totalSize,progress, networkSpeed);
        }
    }

    /**
     * 转成带泛型的对象
     * @param jsonString
     * @param t
     * @param <T>
     * @return
     */
    public  <T>ResponseData jsonToBean(String jsonString, Class<T> t){
        Type objectType = type(ResponseData.class, t);
        return gson.fromJson(jsonString,objectType);
    };


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

}
