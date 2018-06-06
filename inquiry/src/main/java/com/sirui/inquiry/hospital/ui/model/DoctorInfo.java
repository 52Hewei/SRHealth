package com.sirui.inquiry.hospital.ui.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 获得医生列表
 * Created by xiepc on 2016/12/23 13:59
 */

public class DoctorInfo implements Serializable{
    /**医生id*/
    private String doctorId;
    private String doctorName;
    private String historyInquirySum;
    private String hospitalId;
    private String hospitalName;
    private String imId;
    private String onlineStatus;
    private String departmentId;
    private String deptName;
    private String imageTextFlag;
    private String personalGood;
    private String videoFlag;
    private String description;
    private String headImageUrl;
    private String offLineHospitalName;
    private String sex;
    private String titleName;
    private String queueNumber;
   /**active --1:进入候诊室空闲，2:接诊中，3:暂停接诊',9:不在候诊室中*/
    private String active;
    /**是否为登录成功状态 0：未登录  1：登录成功*/
    private String loginStatus;

    public DoctorInfo(JSONObject object){
        this.doctorId = object.optString("doctorId");
        this.doctorName = object.optString("doctorName");
        this.departmentId = object.optString("departmentId");
        this.deptName = object.optString("deptName");
        this.historyInquirySum = object.optString("historyInquirySum");
        this.hospitalId = object.optString("hospitalId");
        this.hospitalName = object.optString("hospitalName");
        this.imId = object.optString("imId");
        this.onlineStatus = object.optString("onlineStatus");
        this.imageTextFlag = object.optString("imageTextFlag");
        this.personalGood = object.optString("personalGood");
        this.videoFlag = object.optString("videoFlag");
        this.description = object.optString("description");
        this.headImageUrl = object.optString("headImageUrl");
        this.offLineHospitalName = object.optString("offLineHospitalName");
        this.sex = object.optString("sex");
        this.titleName = object.optString("doctorTitle");
        this.queueNumber = object.optString("queueNumber");
        this.active = object.optString("active");
        this.loginStatus = object.optString("loginStatus");
    }

    public String getActive() {
        return active;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public DoctorInfo(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffLineHospitalName() {
        return offLineHospitalName;
    }

    public void setOffLineHospitalName(String offLineHospitalName) {
        this.offLineHospitalName = offLineHospitalName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getHistoryInquirySum() {
        return historyInquirySum;
    }

    public void setHistoryInquirySum(String historyInquirySum) {
        this.historyInquirySum = historyInquirySum;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getImageTextFlag() {
        return imageTextFlag;
    }

    public void setImageTextFlag(String imageTextFlag) {
        this.imageTextFlag = imageTextFlag;
    }

    public String getPersonalGood() {
        return personalGood;
    }

    public void setPersonalGood(String personalGood) {
        this.personalGood = personalGood;
    }

    public String getVideoFlag() {
        return videoFlag;
    }

    public void setVideoFlag(String videoFlag) {
        this.videoFlag = videoFlag;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
    }
}
