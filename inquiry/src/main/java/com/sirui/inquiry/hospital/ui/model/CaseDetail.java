package com.sirui.inquiry.hospital.ui.model;

import org.json.JSONObject;

/**
 * Created by xiepc on 2017/4/19 13:40
 */

public class CaseDetail {

    private String chiefComplaint;
    private String doctorAdvice;
    private String pastHistory;
    private String preliminaryDiagnosis;

    public CaseDetail(JSONObject obj){
        this.chiefComplaint = obj.optString("chiefComplaint");
        this.doctorAdvice = obj.optString("doctorAdvice");
        this.pastHistory = obj.optString("pastHistory");
        this.preliminaryDiagnosis = obj.optString("preliminaryDiagnosis");
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getDoctorAdvice() {
        return doctorAdvice;
    }

    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getPreliminaryDiagnosis() {
        return preliminaryDiagnosis;
    }

    public void setPreliminaryDiagnosis(String preliminaryDiagnosis) {
        this.preliminaryDiagnosis = preliminaryDiagnosis;
    }
}
