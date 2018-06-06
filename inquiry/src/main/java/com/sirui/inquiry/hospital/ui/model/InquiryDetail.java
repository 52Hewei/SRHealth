package com.sirui.inquiry.hospital.ui.model;

import org.json.JSONObject;

/**
 * 问诊详情
 * Created by xiepc on 2017/4/19 11:35
 */

public class InquiryDetail {

    private String deptName;
    private String deptId;
    private String doctorId;
    private String doctorName;
    private String hospitalName;
    private String patientName;
    private String sex;
    private String titleName;
    private String historyInquirySum;
    /**医生头像*/
    private String headPortraitUrl;

    private CaseDetail caseDetail;

    public InquiryDetail(JSONObject obj){
        this.deptName = obj.optString("deptName");
        this.deptId = obj.optString("deptId");
        this.doctorId = obj.optString("doctorId");
        this.doctorName = obj.optString("doctorName");
        this.hospitalName = obj.optString("hospitalName");
        this.patientName = obj.optString("patientName");
        this.sex = obj.optString("sex");
        this.titleName = obj.optString("titleName");
        this.headPortraitUrl = obj.optString("headPortraitUrl");
        this.historyInquirySum = obj.optString("historyInquirySum");
        this.caseDetail = new CaseDetail(obj.optJSONObject("caseDetail"));
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getHistoryInquirySum() {
        return historyInquirySum;
    }

    public void setHistoryInquirySum(String historyInquirySum) {
        this.historyInquirySum = historyInquirySum;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
    }
}
