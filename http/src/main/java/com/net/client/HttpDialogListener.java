package com.net.client;

import android.app.Activity;

import com.net.client.event.EventType;
import com.net.client.event.MainEvent;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Response;

/**
 * 字符串请求弹出加载框的监听类
 * Created by xiepc on 2016/11/15.
 */

public abstract class HttpDialogListener extends ABaseListener<String, Response, Exception> {
    private Activity activity;
    private String msg;

    public HttpDialogListener(Activity activity) {
        this.activity = activity;
    }

    public HttpDialogListener(Activity activity, String msg) {
        this.activity = activity;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onFailure(Response response, Exception e) {
        super.onFailure(response, e);

        String eMsg = "";
        if (e != null) {
            eMsg = e.getMessage();
        }
        MainEvent exceptionEvent = new MainEvent(EventType.HTTP_ERROR);
        exceptionEvent.setMsg(eMsg);
        if(response != null){
             exceptionEvent.setCode(response.code());
        }
        EventBus.getDefault().post(exceptionEvent);
    }
}
