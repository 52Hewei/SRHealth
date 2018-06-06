package com.net.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xiepc on 2016/12/23 9:25
 */

public abstract class BaseEntityClass<T> implements BaseListener{

    protected Class<T> entityClass;
    public BaseEntityClass(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    public Class<T> getEntityClass(){
        return entityClass;
    }
}
