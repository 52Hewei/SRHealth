package com.net.client;


/**
 * Created by xiepc on 2016/11/21 20:56
 */

public abstract class HttpBeanListener<T> extends BaseEntityClass<T> {
      public void onStart(){}
      public abstract void onSuccess(T t);
      public void onFailure(String code,String msg){
      }
}
