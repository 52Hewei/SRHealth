package com.sirui.inquiry.hospital.ui.model;


import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 患者请求队列返回排队信息
 * Created by xiepc on 2017/2/15 16:25
 */
public class RequestQueueResult implements Serializable{
    /**患者当前队列中序号*/
    private String count;
    /**IM id编号*/
    private String imId;
    /**im登录token*/
    private String token;
    /**问诊单号*/
    private String orderNo;
    /**预计等待总时长*/
    private String  waitSumDate;
    /**问诊类型*/
    private InquiryTypeEnum inquiryType;
    /**患者主诉*/
    private String chiefComplaint;
    /**启动类型 1：正常问诊，2：恢复问诊*/
    private String startType;
    /**医生总的问诊量*/
    private String historyInquirySum;
    /**聊天记录列表*/
    private List<BaseMessage> imList;
    /**订单状态*/
    private String orderStatus;


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public RequestQueueResult(){
    }
    public RequestQueueResult(JSONObject obj){
        this.count = obj.optString("count");
        this.imId = obj.optString("imId");
        this.orderNo = obj.optString("orderNo");
        this.token = obj.optString("token");
        this.waitSumDate = obj.optString("waitSumDate");
    }

    public List<BaseMessage> getImList() {
        return imList;
    }

    public void setImList(List<BaseMessage> imList) {
        this.imList = imList;
    }

    public String getHistoryInquirySum() {
        return historyInquirySum;
    }

    public void setHistoryInquirySum(String historyInquirySum) {
        this.historyInquirySum = historyInquirySum;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getWaitSumDate() {
        return waitSumDate;
    }

    public void setWaitSumDate(String waitSumDate) {
        this.waitSumDate = waitSumDate;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public InquiryTypeEnum getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(InquiryTypeEnum inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }
}
