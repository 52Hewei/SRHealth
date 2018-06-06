package com.sirui.inquiry.hospital.ui.model;

import org.json.JSONObject;

/**
 * 问诊记录信息
 * Created by xiepc on 2017/2/16 19:43
 */

public class InquiryRecordInfo extends BaseModel {
//    /**问诊单创建时间*/
//    private String createDate;
//   /**创建id*/
//    private String createId;
//    /**科室id*/
//    private String deptId;
//    /**科室名称*/
//    private String deptName;
//    /**问诊医生id*/
//    private String doctorId;
//    /**问诊医生名称*/
//    private String doctorName;
//    /**问诊结束时间*/
//    private String endDate;
//    /**问诊医院id*/
//    private String hospitalId;
//    /**问诊医院名称*/
//    private String hospitalName;
//    /**问诊时间*/
//    private String inquiryDate;
//    /**问诊方式*/
//    private String inquiryType;
//    /**问诊单号*/
//    private String orderNo;
//    /**单号状态*/
//    private String orderStatus;
//    /**患者id*/
//    private String patientId;
//    /**患者名*/
//    private String patientName;
//    /***/
//    private String prescriptionFlag;
//    /**请求队列时间*/
//    private String queueDate;
//    /**更新日期*/
//    private String updateDate;
//
//    private String updateId;


    /**患者主诉*/
    private String chiefComplaint;
    /**初步诊断*/
    private String  preliminaryDiagnosis ;
    /**问诊结束时间*/
    private String endDate;
    /**问诊时间*/
    private String inquiryDate;
    /**问诊方式*/
    private String inquiryType;
    /**问诊单号*/
    private String orderNo;
    /**单号状态*/
    private String orderStatus;
    /**患者名*/
    private String patientName;
    /**请求队列时间*/
    private String queueDate;
    private String age;
    private String sex;
    /**科室名称*/
    private String deptName;
    private String hospitalName;
    private String doctorName;
    /***/
    private String prescriptionFlag;
    /** 订单状态描述信息 */
    private String orderStatusDesc;

    public InquiryRecordInfo() {
    }

    public InquiryRecordInfo(JSONObject object){
        this.age = object.optString("age");
        this.chiefComplaint = object.optString("chiefComplaint");
        this.preliminaryDiagnosis = object.optString("preliminaryDiagnosis");
        this.endDate = object.optString("endDate");
        this.sex = object.optString("sex");
        this.inquiryDate = object.optString("inquiryDate");
        this.inquiryType = object.optString("inquiryType");
        this.orderNo = object.optString("orderNo");
        this.orderStatus = object.optString("orderStatus");
        this.patientName = object.optString("patientName");
        this.queueDate = object.optString("queueDate");
        this.deptName = object.optString("deptName");
        this.hospitalName = object.optString("hospitalName");
        this.doctorName = object.optString("doctorName");
        this.prescriptionFlag = object.optString("prescriptionFlag");
        this.orderStatusDesc = object.optString("orderStatusDesc");
    }

    public String getPreliminaryDiagnosis() {
        return preliminaryDiagnosis;
    }

    public void setPreliminaryDiagnosis(String preliminaryDiagnosis) {
        this.preliminaryDiagnosis = preliminaryDiagnosis;
    }

    public String getPrescriptionFlag() {
        return prescriptionFlag;
    }

    public void setPrescriptionFlag(String prescriptionFlag) {
        this.prescriptionFlag = prescriptionFlag;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }
}
