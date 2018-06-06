package com.sirui.inquiry.hospital.bean;

import java.util.List;

/**
 * 问诊记录处方详情对象
 * <p>
 * Created by yellow on 2017/3/20.
 */

public class PrescriptionData {

    /**
     * 初步诊断
     */
    private String firstDiagnose;

    /**
     * 处理意见
     */
    private String advice;

    /**
     * 药品处方列表
     */
    private List<MedicineData> prescriptionDrugList;
    /**
     * 处方状态
     */
    private String prescriptionStatus;

    public String getFirstDiagnose() {
        return firstDiagnose;
    }

    public void setFirstDiagnose(String firstDiagnose) {
        this.firstDiagnose = firstDiagnose;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    /**
     * 获取处方药品列表
     * @return 处方药品列表
     */
    public List<MedicineData> getPrescriptionDrugList() {
        return prescriptionDrugList;
    }

    public void setPrescriptionDrugList(List<MedicineData> prescriptionDrugList) {
        this.prescriptionDrugList = prescriptionDrugList;
    }

    public String getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
}
