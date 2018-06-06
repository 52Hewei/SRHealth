package com.net.client;

/**
 * Created by xiepc on 2016/12/23 12:59
 */

public abstract class ABaseListener<T,R,E> implements BaseListener{
    public void onStart(){}
    public abstract void onSuccess(T t);
    public void onFailure(R r,E e){}
}
