package com.sirui.basiclib.http;

import android.app.Activity;

import com.net.client.HttpBeanListener;
import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.bean.PublicKey;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.basiclib.utils.string.StringUtil;

import org.json.JSONObject;

import okhttp3.Response;

import static com.sirui.basiclib.config.SRConstant.APP_ID;
import static com.sirui.basiclib.config.SRConstant.APP_ID_KEY;
import static com.sirui.basiclib.config.SRConstant.DATA;


/**
 * Http 接口请求加密密钥管理
 *
 * Created by yellow on 17/6/22.
 */

public class HttpKeyManager {

    private static HttpKeyManager instance;

    /** 整体加密密钥 KEY */
    private final static String PUBLIC_KEY_DATA = "publicKey1";
    /** 字段加密密钥 KEY */
    private final static String PUBLIC_KEY_FIELD = "publicKey2";

    /** 整体加密密钥 */
    private static String PUB_KEY_DATA_CONTENT = "";
    /** 字段加密密钥 */
    private static String PUB_KEY_FIELD_CONTENT = "";

//    private SharedPreferences keySp;
//    private final static String SP_NAME = "RSA_NAME";
//    private final static String KEY_UPDATE_TIMESTAMP = "KEY_UPDATE_TIMESTAMP";

    private HttpKeyManager() {
    }

    public static HttpKeyManager getInstance() {
        if (instance == null) {
            instance = new HttpKeyManager();
        }
        return instance;
    }

    public void updateRsaKey() {
        HttpGo.post(NetUrl.URL_GET_PUBLIC_KEY)
                .params(APP_ID_KEY, APP_ID)
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        parseRsult(s);
                    }
                });
    }

    public void getRsaKey(Activity activity, final OnGetRsaKeyListener onGetRsaKeyListener){
        HttpGo.post(NetUrl.URL_GET_PUBLIC_KEY)
                .tag(this)
                .params(APP_ID_KEY,APP_ID)
                .execute(new HttpDialogListener(activity,"初始化数据中,请稍后..."){
                    @Override
                    public void onSuccess(String s) {
                        boolean isSuccess = parseRsult(s);
                        if(onGetRsaKeyListener != null){
                              if(isSuccess){
                                  onGetRsaKeyListener.onSuccess();
                              }else{
                                  onGetRsaKeyListener.onFailure();
                              }
                        }
                    }

                    @Override
                    public void onFailure(Response response, Exception e) {
                        super.onFailure(response, e);
                        if(onGetRsaKeyListener != null){
                            onGetRsaKeyListener.onFailure();
                        }
                    }
                });
    }

    public void getRsaKey2(){
        HttpGo.post(NetUrl.URL_GET_PUBLIC_KEY)
                .tag(this)
                .params(APP_ID_KEY,APP_ID)
                .execute(new HttpBeanListener<PublicKey>(){
                    @Override
                    public void onSuccess(PublicKey publicKey) {
                        if(publicKey != null){
                            MyLog.i("public1=========" + publicKey.getPublicKey1());
                        }
                    }
                }
        );
    }

    private boolean parseRsult(String s){
        String pubData = "";
        String pubField = "";
//        JSONObject responseJson = JsonUtil.checkResult(s);
        JSONObject responseJson = JsonUtil.checkResult(s);
        if (responseJson != null && responseJson.opt(DATA) != null) {
            JSONObject dataJson = responseJson.optJSONObject(DATA);
            pubData = dataJson.optString(PUBLIC_KEY_DATA);
            pubField = dataJson.optString(PUBLIC_KEY_FIELD);
        }else{
            return false;
        }
        if (!StringUtil.isEmpty(pubData) && !StringUtil.isEmpty(pubField)) {
            PUB_KEY_DATA_CONTENT = pubData;
            PUB_KEY_FIELD_CONTENT = pubField;
        }else{
            return false;
        }
        SPUtil.putString(SRConstant.PUBLIC_KEY,PUB_KEY_DATA_CONTENT);
        SPUtil.putString(SRConstant.PUBLIC_FILED,PUB_KEY_FIELD_CONTENT);
        return true;
    }


    public String getFieldRsaKey() {
        return SPUtil.getString(SRConstant.PUBLIC_FILED);
    }

    public String getDataRsaKey() {
        return SPUtil.getString(SRConstant.PUBLIC_KEY);
    }

    public interface OnGetRsaKeyListener{
        void onSuccess();
        void onFailure();
    }
}
