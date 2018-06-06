package com.sirui.inquiry.hospital.ui.model;


import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.model.TextMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiepc on 2017/5/23 11:35
 */

public class InquiryOrderDetail {

    private String createDate;
    private String createId;
    private String deptId;
    private String deptName;
    private String doctorIMId;
    private String doctorId;
    private String doctorName;
    private String enterpriseId;
    private String hospitalId;
    private String hospitalName;
    private String inquiryDate;
    private InquiryTypeEnum inquiryType;
    private String orderNo;
    private String orderStatus;
    private String patientId;
    private String patientImId;
    private String patientImToken;
    private String patientName;
    private String queueDate;
    private String storeId;
    private String updateDate;
    private String updateId;
    /** 医生历史问诊量 */
    private String historyInquirySum;
    /** 患者主诉 */
    private String chiefComplaint;
    /**预计等待时长*/
    private String waitSumDate;
    /**患者当前队列中序号*/
    private String queueCount;

    private List<BaseMessage> imList = new ArrayList<>();


    public InquiryOrderDetail(JSONObject object){
        this.createDate = object.optString("createDate");
        this.createId = object.optString("createId");
        this.deptId = object.optString("deptId");
        this.deptName = object.optString("deptName");
        this.doctorIMId = object.optString("doctorIMId");
        this.doctorId = object.optString("doctorId");
        this.doctorName = object.optString("doctorName");
        this.enterpriseId = object.optString("enterpriseId");
        this.hospitalId = object.optString("hospitalId");
        this.hospitalName = object.optString("hospitalName");

        this.inquiryDate = object.optString("inquiryDate");
          String type = object.optString("inquiryType");
        if(InquiryTypeEnum.typeText.getValue().equals(type)){
            this.inquiryType = InquiryTypeEnum.typeText;
        }else{
            this.inquiryType = InquiryTypeEnum.typeVideo;
        }
        this.orderNo = object.optString("orderNo");
        this.orderStatus = object.optString("orderStatus");

        this.patientId = object.optString("patientId");
        this.patientImId = object.optString("patientImId");
        this.patientName = object.optString("patientName");
        this.patientImToken = object.optString("patientImToken");
        this.queueDate = object.optString("queueDate");

        this.storeId = object.optString("storeId");
        this.updateDate = object.optString("updateDate");
        this.updateId = object.optString("updateId");

        this.historyInquirySum = object.optString("historyInquirySum");
        this.waitSumDate = object.optString("waitSumDate");
        this.queueCount = object.optString("queueCount");

        JSONObject caseJson = object.optJSONObject("caseDetail");
        if (caseJson != null) {
            this.chiefComplaint = caseJson.optString("chiefComplaint");
        }

        JSONArray imArray = object.optJSONArray("imList");
        if(imArray != null && imArray.length() > 0){
            for (int i = 0; i < imArray.length(); i++) {
                BaseMessage msg = new TextMessage(imArray.optJSONObject(i));
                msg.setStatus(MsgStatusEnum.success); //必须要把消息设置为发送成功状态，否则会显示不出来(消息状态设置switch语句引用将为空,异常)
                imList.add(msg);
            }
        }
    }

    public String getWaitSumDate() {
        return waitSumDate;
    }

    public void setWaitSumDate(String waitSumDate) {
        this.waitSumDate = waitSumDate;
    }

    public String getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(String queueCount) {
        this.queueCount = queueCount;
    }

    public List<BaseMessage> getImList() {
        return imList;
    }

    public void setImList(List<BaseMessage> imList) {
        this.imList = imList;
    }

    public String getPatientImToken() {
        return patientImToken;
    }

    public void setPatientImToken(String patientImToken) {
        this.patientImToken = patientImToken;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDoctorIMId() {
        return doctorIMId;
    }

    public void setDoctorIMId(String doctorIMId) {
        this.doctorIMId = doctorIMId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public InquiryTypeEnum getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(InquiryTypeEnum inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientImId() {
        return patientImId;
    }

    public void setPatientImId(String patientImId) {
        this.patientImId = patientImId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(String queueDate) {
        this.queueDate = queueDate;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getHistoryInquirySum() {
        return historyInquirySum;
    }

    public void setHistoryInquirySum(String historyInquirySum) {
        this.historyInquirySum = historyInquirySum;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }
}
