package com.sirui.basiclib.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.net.client.event.EventType;
import com.net.client.event.MainEvent;
import com.sirui.basiclib.widget.MyToast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by xiepc on 2016/12/23 8:43
 */
public class JsonUtil {


    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static void checkResultTip(String result){
        try{
            JSONObject obj = new JSONObject(result);

            String errCode = obj.getString("errCode");
            String errDesc = obj.getString("errDesc");
            if (!obj.isNull("errCode")){
                if (errCode.equals("000000"))
                    return;
                if (errCode.equals("000002")){
                    EventBus.getDefault().post(new MainEvent(EventType.OTHER_LOGIN));
                }else if(errCode.equals("300005")){

                }else{
                    MyToast.show(obj.getString("errDesc"));
                }

            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static JSONObject checkResult(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            if (!obj.isNull("errCode") && "000000".equals(obj.getString("errCode"))) {
                return obj;
            } else if (!obj.isNull("errCode") && "000002".equals(obj.getString("errCode"))) { //token失效
                EventBus.getDefault().post(new MainEvent(EventType.OTHER_LOGIN));
            }else if(!obj.isNull("errCode") && "300005".equals(obj.getString("errCode"))){

            }else{
                MyToast.show(obj.getString("errDesc"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T checkToGetData(String result, Class<T> cls) {
        try {
             JSONObject obj = new JSONObject(result);
            if (!obj.isNull("errCode") && "000000".equals(obj.getString("errCode"))) {
                if(!obj.isNull("data")){
                    return gsonToBean(obj.getString("data"),cls);
                }else{

                }
            } else if (!obj.isNull("errCode") && "000002".equals(obj.getString("errCode"))) { //会员token失效
                 EventBus.getDefault().post(new MainEvent(EventType.OTHER_LOGIN));
            }else if(!obj.isNull("errCode") && "300005".equals(obj.getString("errCode"))){

            }else {
                MyToast.show(obj.getString("errDesc"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        }
        return list;
    }

    public static String Object2Json(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 判断一个tip消息的操作类型
     *
     * @param msg
     * @return
     */
    public static String getTipMsgOperateType(String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            if (!object.isNull("operateType")) {
                return object.optString("operateType");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得tip消息的附加数据
     *
     * @param msg
     * @return
     */
    public static JSONObject getTipMsgData(String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            if (!object.isNull("data")) {
                return object.optJSONObject("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getErrCode(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            if (!obj.isNull("errCode")) {
                return obj.getString("errCode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getData(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            if (!obj.isNull("data")) {
                return obj.getString("data");
            } else {
                Log.i("sirui","data为空");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成一个Json格式的tip消息
     *
     * @param operateType 操作类型
     * @param data        可以附加其它的数据
     * @return
     */
    public static String createTipMsg(String operateType, JSONObject data) {
        JSONObject object = new JSONObject();
        try {
            object.put("operateType", operateType);
            if (data != null) {
                object.put("data", data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
