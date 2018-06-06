package com.net.client.bean;

/**
 * Created by xiepc on 2016/11/21 20:58
 */

public class ResponseData<T>{

    private String result;
    private String errCode;
    private String errDesc;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "result='" + result + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errDesc='" + errDesc + '\'' +
                ", PatientCaseBean=" + data +
                '}';
    }
}
