package com.sirui.basiclib.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xiepc on 2018/5/29 18:29
 */
public class PayBean implements Serializable{
    /**
     *   "appId": "wx368be98026f3551c",
     "discountAmount": "200",
     "nonceStr": "ca15790909a04f61a23b39b29a5d1ce3",
     "orderNo": "20180529036769152",
     "package": "prepay_id=wx2917555472406338db110a2c2066160920",
     "paySign": "5904A7E93043EFE59391EDF03D09F032D079F480F966A4620FCF762A219C1DBA",
     "paymentAmount": "201",
     "paymentNo": "252371e84ead4158b73eedfbc2769b5d",
     "prepay_id": "wx2917555472406338db110a2c2066160920",
     "signType": "HMACSHA256",
     "timeStamp": "1527587752",
     "totalAmount": "1",
     "trade_type": "JSAPI",
     "transctionNo": "1527587755397"
     */
    String appId;
    String discountAmount;
    String nonceStr;
    String orderNo;
    @SerializedName("package")
    String packageName;
    String paySign;
    String paymentAmount;
    String paymentNo;
    String prepay_id;
    String signType;
    String timeStamp;
    String totalAmount;
    String trade_type;
    String transctionNo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTransctionNo() {
        return transctionNo;
    }

    public void setTransctionNo(String transctionNo) {
        this.transctionNo = transctionNo;
    }
}
